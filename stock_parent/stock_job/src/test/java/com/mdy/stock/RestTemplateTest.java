package com.mdy.stock;

import com.mdy.stock.pojo.domain.InnerMarketDomain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

/**
 * @author mdy
 * @date 2024-06-24 14:17
 * @description
 */
@SpringBootTest
public class RestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void responseEntityTest() {
        String url = "http://localhost:8090/api/quot/index/all";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//        System.out.println(response.getBody());
//        System.out.println(response.getHeaders());
//        System.out.println(response.getStatusCodeValue());
//        System.out.println(response);
    }

    @Test
    public void DomainTest() {
        String url = "http://localhost:8090/api/quot/index/all";
        InnerMarketDomain innerMarketInfo = restTemplate.getForObject(url, InnerMarketDomain.class);
        System.out.println(innerMarketInfo);
    }
}
