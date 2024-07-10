package com.mdy.stock.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mdy
 * @date 2024-04-16 18:31
 * @description 配置redis
 */

@Configuration
@EnableCaching
public class RedisCacheConfig {

    /**
     * 配置 cacheManager 代替默认的cacheManager（缓存管理器）
     * @param factory RedisConnectionFactory
     * @return CacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 建立序列化器
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // 设置ObjectMapper，用于配置JSON序列化的行为，比如属性的可见性、类型信息的保存等。
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 仅仅序列化对象的属性，且属性不可为final修饰
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jsonRedisSerializer.setObjectMapper(objectMapper);

        // 配置key和value序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer))
                // 关闭控制存储
                .disableCachingNullValues()
                // 修改前缀与key的间隔符号，默认是::
                .computePrefixWith(cacheName -> cacheName + ":");

        // 设置特有的Redis配置
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        // 定制化的Cache设置过期时间就，表示"role:"开头的缓存存活时间为10s
        cacheConfigurations.put("role", customRedisCacheConfiguration(config, Duration.ofSeconds(10)));
        cacheConfigurations.put("stock", customRedisCacheConfiguration(config, Duration.ofSeconds(3000)));
        cacheConfigurations.put("market", customRedisCacheConfiguration(config, Duration.ofSeconds(300)));

        // 构建redis缓存管理器
        return RedisCacheManager.builder(factory)
                .transactionAware() // Cache事务支持
                .withInitialCacheConfigurations(cacheConfigurations)
                .cacheDefaults(config)
                .build();
    }

    /**
     * 设置RedisConfiguration配置
     * @param config redis的config配置
     * @param ttl 缓存存活时间
     * @return 设置好的RedisCacheConfiguration
     */
    public RedisCacheConfiguration customRedisCacheConfiguration(RedisCacheConfiguration config, Duration ttl) {
        // 设置缓存缺省超时时间
        return config.entryTtl(ttl);
    }

    /**
     * 配置redisTemplate bean，自定义数据的序列化的方式
     * @param redisConnectionFactory 连接redis的工厂，底层有场景依赖启动时，自动加载
     * @return Redis模板
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory) {
        // 1.构建RedisTemplate模板对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // 2.为不同的数据结构设置不同的序列化方案
        // 设置key序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value序列化方式
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        // 设置hash中field字段序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        // 设置hash中value的序列化方式
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        // 5.初始化参数设置
        template.afterPropertiesSet();
        return template;
    }
}
