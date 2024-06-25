package com.mdy.stock.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mdy.stock.POJO.InnerMarketDO;
import com.mdy.stock.mapper.StockMarketIndexInfoMapper;
import com.mdy.stock.mapper.StockOuterMarketIndexInfoMapper;
import com.mdy.stock.mapper.StockRtInfoMapper;
import com.mdy.stock.pojo.entity.StockMarketIndexInfo;
import com.mdy.stock.pojo.entity.StockRtInfo;
import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.service.StockTimerTaskService;
import com.mdy.stock.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.compiler.ast.StringL;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private String getMarketNameByCode(String code) {
        switch (code) {
            case "sh000001": return "上证指数";
            case "sz399001": return "深圳指数";
            default: return "未知国内指数";
        }
    }

    /**
     * 获取国内大盘的实时数据信息
     */
    @Override
    public void getAndInsertInnerMarketInfo() {
        List<String> marketCodeList = stockInfoConfig.getInnerMarketId();
        // 组装请求对象
        HttpHeaders headers = new HttpHeaders();
        // 请求头设置，绕开屏蔽
        headers.add("Referer", "https://finance.sina.com.cn/stock/");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        // stockMarketIndexInfos用于存储读取到的国内大盘数据
        List<StockMarketIndexInfo> stockMarketIndexInfos = new ArrayList<>();

        // 解析字符串，将数据载入StockMarketIndexInfo对象，最后将该对象添加进列表中
        String reg = "var hq_str_(.+)=\"(.+)\"";
        // 编译表达式,获取编译对象
        Pattern pattern = Pattern.compile(reg);

        for (String code : marketCodeList) {
            // 组装请求大盘数据的url
            String url = stockInfoConfig.getInnerMarketUrl() + code;
            // 发起请求获取数据
            String marketInfoStr = restTemplate.postForObject(url, entity, String.class);
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
                        .curTime(DateTime.parse(info.get(30) + " " + info.get(31), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate())
                        .build();
                stockMarketIndexInfos.add(marketInfo);
            } else {
                log.error("无法读取远程数据库的股票指数信息，原因：字符串解析出错");
                return;
            }
        }
        // 向数据库批量导入大盘指数信息
        int insertCnt =  stockOuterMarketIndexInfoMapper.insertStockInfosPatch(stockMarketIndexInfos);
        log.info("当前时间:{} 插入了{}条国内大盘指数数据", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), insertCnt);
    }


    /**
     * 获取沪深两市所有个股的实时数据信息，并向数据库插入该数据
     */
    @Override
    public void getAndInsertStockInfo() {
        // 获取个股列表
        List<String> stockCodeList = stockRtInfoMapper.findStockCodeList();

        // 组装请求对象
        HttpHeaders headers = new HttpHeaders();
        // 请求头设置，绕开屏蔽
        headers.add("Referer", "https://finance.sina.com.cn/stock/");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        // 编辑reg匹配信息
        String reg = "var hq_str_(.+)=\"(.+)\"";
        // 编译表达式,获取编译对象
        Pattern pattern = Pattern.compile(reg);

        List<StockRtInfo> stockRtInfos = new ArrayList<>();
        // 一次获取20个股票数据，降低网络、磁盘IO负担
        Lists.partition(stockCodeList, 20).forEach(list -> {
            for (String code : list) {
                String url = stockInfoConfig.getAStockUrl() + (code.startsWith("00") ? "sz" : "sh") + code;
                String marketInfoStr = restTemplate.postForObject(url, entity, String.class);
                Matcher matcher = pattern.matcher(marketInfoStr);
                if (matcher.find()) {
                    List<String> info = Arrays.asList(matcher.group(2).split(","));
                    StockRtInfo stockRtInfo = StockRtInfo.builder()
                            .id(idWorker.nextId())
                            .stockCode(code)
                            .stockName(info.get(0))
                            .openPrice(new BigDecimal(info.get(1)))
                            .preClosePrice(new BigDecimal(info.get(2)))
                            .curPrice(new BigDecimal(info.get(3)))
                            .maxPrice(new BigDecimal(info.get(4)))
                            .minPrice(new BigDecimal(info.get(5)))
                            .tradeAmount(Long.valueOf(info.get(8)))
                            .tradeVolume(new BigDecimal(info.get(9)))
                            .curTime(DateTime.parse(info.get(30) + " " + info.get(31), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate())
                            .build();
                    stockRtInfos.add(stockRtInfo);
                } else {
                    log.error("无法读取远程数据库的股票指数信息，原因：字符串解析出错");
                    return;
                }
            }
        });

        // 将数据批量添加进数据库
        int insertCnt = stockRtInfoMapper.insertStockRtInfoList(stockRtInfos);
        log.info("当前时间:{} 插入了{}条国内大盘指数数据", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), insertCnt);
    }


}
