package com.mdy.stock.service.impl;

import com.google.common.collect.Lists;
import com.mdy.stock.mapper.StockOuterMarketIndexInfoMapper;
import com.mdy.stock.mapper.StockRtInfoMapper;
import com.mdy.stock.pojo.entity.StockMarketIndexInfo;
import com.mdy.stock.pojo.entity.StockRtInfo;
import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.service.StockTimerTaskService;
import com.mdy.stock.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author mdy
 * @date 2024-06-24 17:09
 * @description
 */

@Service
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {

    @Resource
    StockInfoConfig stockInfoConfig;

    @Resource
    RestTemplate restTemplate;

    @Resource
    IdWorker idWorker;

    @Resource
    StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;

    @Resource
    StockRtInfoMapper stockRtInfoMapper;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final Object lock = new Object();

    /**
     * 组装请求对象
     * @return HttpEntity
     */
    private HttpEntity<Object> composeHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        // 这样设置是为了绕开屏蔽
        headers.add("Referer", "https://finance.sina.com.cn/stock/");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        return new HttpEntity<>(headers);
    }

    /**
     * 获取国内大盘的实时数据信息
     */
    @Override
    public void getAndInsertInnerMarketInfo() {
        // stockMarketIndexInfos用于存储读取到的国内大盘数据
        List<StockMarketIndexInfo> stockMarketIndexInfos = new ArrayList<>();
        // 从配置文件中读取大盘id数据
        List<String> marketCodeList = stockInfoConfig.getInnerMarketId();
        // 获取http请求entity
        HttpEntity<Object> httpEntity =  composeHttpEntity();
        // 配置正则表达式
        Pattern pattern = Pattern.compile("var hq_str_(.+)=\"(.+)\"");

        // 解析字符串，将数据载入StockMarketIndexInfo对象，最后将该对象添加进列表中
        for (String code : marketCodeList) {
            // 组装请求大盘数据的url
            String url = stockInfoConfig.getInnerMarketUrl() + code;
            // 发起请求获取数据
            String marketInfoStr = restTemplate.postForObject(url, httpEntity, String.class);
            if (marketInfoStr == null || marketInfoStr.length() < 20) {
                log.error("无法读取远程数据库的大盘指数信息，原因：信息获取失败！");
                return;
            }
            // 匹配字符串
            Matcher matcher = pattern.matcher(marketInfoStr);
            if (matcher.find()) {
                List<String> info = Arrays.asList(matcher.group(2).split(","));
                StockMarketIndexInfo marketInfo = StockMarketIndexInfo.builder()
                        .id(idWorker.nextId())
                        .marketCode(matcher.group(1))
                        .marketName(info.get(0))
                        .openPoint(new BigDecimal(info.get(1)))
                        .preClosePoint(new BigDecimal(info.get(2)))
                        .curPoint(new BigDecimal(info.get(3)))
                        .maxPoint(new BigDecimal(info.get(4)))
                        .minPoint(new BigDecimal(info.get(5)))
                        .tradeAmount(Long.valueOf(info.get(8)))
                        .tradeVolume(new BigDecimal(info.get(9)))
                        .curTime(DateTime.parse(info.get(30) + " " + (info.get(31).substring(0, info.get(31).length() - 2) + "00"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate())
                        .build();
                stockMarketIndexInfos.add(marketInfo);
            } else {
                log.error("无法读取远程数据库的大盘指数信息，原因：字符串解析出错");
                return;
            }
        }

        // 向数据库批量导入大盘指数信息
        int insertCnt =  stockOuterMarketIndexInfoMapper.insertStockInfosPatch(stockMarketIndexInfos);
        log.info("当前时间:{} 插入了{}条国内大盘指数数据", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), insertCnt);

        // 向主服务发送信息，告知现在需要刷新数据
        rabbitTemplate.convertAndSend("stockExchange", "inner.market", new Date());
    }


    /**
     * 获取沪深两市所有个股的实时数据信息，并向数据库插入该数据
     */
    @Override
    public void getAndInsertStockInfo() {
        // 获取个股编码列表
        List<String> stockCodeList = stockRtInfoMapper.findStockCodeList();
        // 处理个股编码
        for (int i = 0; i < stockCodeList.size(); i++) {
            String code = (stockCodeList.get(i).startsWith("00") ? "sz" : "sh") + stockCodeList.get(i);
            stockCodeList.set(i, code);
        }
        // 组装请求对象
        HttpEntity<Object> httpEntity = composeHttpEntity();
        // 编译表达式并获取编译对象
        Pattern pattern = Pattern.compile("var hq_str_(.+)=\"(.+)\"");

        // 一次获取15个股票数据，降低网络、磁盘IO负担

        Lists.partition(stockCodeList, 15).forEach(list -> {
            // 每个分片的数据开启一个线程异步执行任务
            threadPoolTaskExecutor.execute(() -> {
                List<StockRtInfo> stockRtInfos = new ArrayList<>();
                String url = stockInfoConfig.getAStockUrl() + String.join(",", list);
                String marketInfoStr = restTemplate.postForObject(url, httpEntity, String.class);
                if (marketInfoStr == null || marketInfoStr.length() < 20) {
                    log.error("无法读取远程数据库的股票信息，原因：信息获取失败！");
                    return;
                }

                // 解析数据并封装进实体类
                marketInfoStr = marketInfoStr.replaceAll("\n", "");
                String[] marketInfoList = marketInfoStr.split(";");
                for (String infoStr : marketInfoList) {
                    // 匹配字符串
                    Matcher matcher = pattern.matcher(infoStr);
                    if (matcher.find()) {
                        List<String> info = Arrays.asList(matcher.group(2).split(","));
                        StockRtInfo stockRtInfo = StockRtInfo.builder()
                                .id(idWorker.nextId())
                                .stockCode(matcher.group(1).substring(2))
                                .stockName(info.get(0))
                                .openPrice(new BigDecimal(info.get(1)))
                                .preClosePrice(new BigDecimal(info.get(2)))
                                .curPrice(new BigDecimal(info.get(3)))
                                .maxPrice(new BigDecimal(info.get(4)))
                                .minPrice(new BigDecimal(info.get(5)))
                                .tradeAmount(Long.valueOf(info.get(8)))
                                .tradeVolume(new BigDecimal(info.get(9)))
                                .curTime(DateTime.parse(info.get(30) + " " + (info.get(31).substring(0, info.get(31).length() - 2) + "00"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate())
                                .build();
                        stockRtInfos.add(stockRtInfo);
                    } else {
                        log.error("无法读取远程数据库的股票信息，原因：字符串解析出错");
                        return;
                    }
                }
                synchronized (lock) {
                    // 将数据批量添加进数据库
                    int insertCnt = stockRtInfoMapper.insertStockRtInfoList(stockRtInfos);
                    log.info("当前时间:{} 插入了{}条国内股票数据", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), insertCnt);
                    // 向主服务发送信息，告知现在需要刷新数据
                    rabbitTemplate.convertAndSend("stockExchange", "inner.stock", new Date());
                }
            });
        });
    }


}
