package com.mdy.stock.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author mdy
 * @date 2024-06-26 0:43
 * @description
 */
@Configuration
public class CacheConfig {

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置caffeineCache
     * @return caffeineCache
     */
    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine
                .newBuilder()
                .maximumSize(200)
//                .expireAfterAccess(1, TimeUnit.SECONDS)
                .initialCapacity(100)
                .build();
    }
}
