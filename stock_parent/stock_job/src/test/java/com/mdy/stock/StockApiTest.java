package com.mdy.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mdy.stock.POJO.InnerMarketDO;
import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author mdy
 * @date 2024-06-24 16:51
 * @description
 */
@SpringBootTest
public class StockApiTest {

    @Autowired
    StockInfoConfig stockInfoConfig;

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void test1() {
        List<String> marketCodeList = stockInfoConfig.getInnerMarketId();
        String marketUrl = stockInfoConfig.getMarketUrl() + marketCodeList.get(0) + "/96bb56268f791a1a63";
        InnerMarketDO innerStockData = restTemplate.getForObject(marketUrl, InnerMarketDO.class);
        System.out.println(innerStockData);
    }

    @Autowired
    StockTimerTaskService stockTimerTaskService;

    @Test
    public void test2() {
        stockTimerTaskService.getInnerMarketInfo();
    }

    @Test
    public void test3()  {


        stockTimerTaskService.getStockInfo();
    }
}
