package com.mdy.stock;

import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
        ResponseEntity<String> response = restTemplate.getForEntity(marketUrl, String.class);
        System.out.println(response.getBody());
    }
}
