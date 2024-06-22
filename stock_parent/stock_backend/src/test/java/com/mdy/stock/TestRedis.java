package com.mdy.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author mdy
 * @date 2024-06-22 11:03
 * @description
 */
@SpringBootTest
public class TestRedis {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedis() {
        System.out.println(redisTemplate.opsForValue().get("mdy"));
    }
}
