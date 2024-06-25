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

        // 向数据库批量导入大盘指数信息
        stockOuterMarketIndexInfoMapper.insertStockInfosPatch(stockMarketIndexInfos);
    }


    /**
     * 获取沪深两市个股的实时数据信息
     */
    @Override
    public void getStockInfo() {
        // 获取股票列表

    }


}
