package com.mdy.stock;

import com.mdy.stock.POJO.InnerStockDataDO;
import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.service.StockTimerTaskService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.List;

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
    public void test() {
        List<String> marketCodeList = stockInfoConfig.getInnerMarketId();
        String marketUrl = stockInfoConfig.getMarketUrl() + marketCodeList.get(0) + "/96bb56268f791a1a63";
        InnerStockDataDO innerStockData = restTemplate.getForObject(marketUrl, InnerStockDataDO.class);
        System.out.println(innerStockData);
    }

    @Autowired
    StockTimerTaskService stockTimerTaskService;

    @Test
    public void test2() throws ParseException {
        stockTimerTaskService.getInnerMarketInfo();
    }
}
