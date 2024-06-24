package com.mdy.stock.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mdy.stock.POJO.InnerMarketDO;
import com.mdy.stock.mapper.StockOuterMarketIndexInfoMapper;
import com.mdy.stock.mapper.StockRtInfoMapper;
import com.mdy.stock.pojo.entity.StockMarketIndexInfo;
import com.mdy.stock.pojo.entity.StockRtInfo;
import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.service.StockTimerTaskService;
import com.mdy.stock.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
    public void getInnerMarketInfo() {
        // 组装请求大盘数据的url
        List<String> marketCodeList = stockInfoConfig.getInnerMarketId();
        List<StockMarketIndexInfo> stockMarketIndexInfos = new ArrayList<>();

        // 请求数据，并将获取的数据封装到数据库表实体类StockMarketIndexInfo中
        for (String code : marketCodeList) {
            String marketUrl = stockInfoConfig.getMarketUrl() + code + "/" + stockInfoConfig.getLicence();
            InnerMarketDO innerMarketDO = restTemplate.getForObject(marketUrl, InnerMarketDO.class);
            if (innerMarketDO == null) {
                log.error("当前时间点：{} 采集数据失败。可能是因为licence受限，或者第三方的URL改变", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }
            log.info("当前时间点：{} 采集到的数据：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), innerMarketDO);
            // 将日期字符串转换为date类型
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Objects.requireNonNull(innerMarketDO).getT());
            } catch (ParseException parseException) {
                log.error("当前时间点：{} date数据转换失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }
            // 导入数据到实体类
            StockMarketIndexInfo stockMarketIndexInfo = new StockMarketIndexInfo();
            stockMarketIndexInfo.setId(idWorker.nextId());
            stockMarketIndexInfo.setMarketCode(code);
            stockMarketIndexInfo.setMarketName(getMarketNameByCode(code));
            stockMarketIndexInfo.setCurTime(date);
            stockMarketIndexInfo.setPreClosePoint(Objects.requireNonNull(innerMarketDO).getYc());
            stockMarketIndexInfo.setCurPoint(innerMarketDO.getP());
            stockMarketIndexInfo.setOpenPoint(innerMarketDO.getO());
            stockMarketIndexInfo.setMaxPoint(innerMarketDO.getH());
            stockMarketIndexInfo.setMinPoint(innerMarketDO.getL());
            stockMarketIndexInfo.setTradeAmount(innerMarketDO.getV().longValue());
            stockMarketIndexInfo.setTradeVolume(innerMarketDO.getCje());
            stockMarketIndexInfos.add(stockMarketIndexInfo);
        }
        // 向数据库批量导入大盘指数信息
        stockOuterMarketIndexInfoMapper.insertStockInfosPatch(stockMarketIndexInfos);
    }


    /**
     * 获取沪深两市个股的实时数据信息
     */
    @Override
    public void getStockInfo() {
        // 获取股票列表
        String marketCodeUrl = stockInfoConfig.getStockCodeUrl() + stockInfoConfig.getLicence();
        String result = restTemplate.getForObject(marketCodeUrl, String.class);
        if ("101".equals(result)) {
            log.error("当前时间点：{} 证书到期，无法再获取数据", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            return;
        }
        List<Map<String, String>> stockCodeList = new ArrayList<>();
        try {
            stockCodeList = new ObjectMapper().readValue(result, List.class);
            // 只要上海和深圳两市的股票数据
            stockCodeList.removeIf(map -> !"sh".equals(map.get("jys")) && !"sz".equals(map.get("jys")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<StockRtInfo> stockRtInfoList = new ArrayList<>();
        // 根据股票列表查询股票数据
        Lists.partition(stockCodeList, 20).forEach(list -> {
            for (Map<String, String> stockInfo : list) {
                // 组装URL，从远程API中获取数据
                String marketUrl = stockInfoConfig.getStockUrl() + stockInfo.get("dm") + "/" + stockInfoConfig.getLicence();
                InnerMarketDO innerStockData = restTemplate.getForObject(marketUrl, InnerMarketDO.class);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("当前时间点：{} 采集到的数据：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), innerStockData);
                // 将字符串转成Date类型
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Objects.requireNonNull(innerStockData ).getT());
                } catch (ParseException parseException) {
                    log.error("当前时间点：{} date数据转换失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
                }
                // 将获取到的数据注入到实体类对象中
                StockRtInfo stockRtInfo = StockRtInfo.builder()
                        .id(idWorker.nextId())
                        .stockCode(stockInfo.get("dm"))
                        .stockName(stockInfo.get("mc"))
                        .curTime(date)
                        .preClosePrice(Objects.requireNonNull(innerStockData).getYc())
                        .openPrice(innerStockData.getO())
                        .curPrice(innerStockData.getP())
                        .maxPrice(innerStockData.getH())
                        .minPrice(innerStockData.getL())
                        .tradeAmount(innerStockData.getV().longValue())
                        .tradeVolume(innerStockData.getCje())
                        .build();
                stockRtInfoList.add(stockRtInfo);
            }
        });
        // 将数据批量添加到数据库中
        stockRtInfoMapper.insertStockRtInfoList(stockRtInfoList);

    }


}
