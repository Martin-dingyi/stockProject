package com.mdy.stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author mdy
 * @date 2024-06-24 14:15
 * @description 配置http客户端
 */

@Configuration
public class HttpClientConfig {

    /**
     * 定义restTemplate bean
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


}
