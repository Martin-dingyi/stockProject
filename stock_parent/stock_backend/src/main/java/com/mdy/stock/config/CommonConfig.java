package com.mdy.stock.config;

import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author mdy
 * @date 2024-04-16 9:40
 * @description 一般配置类，用于实例化一般工具
 */

@Configuration
@EnableConfigurationProperties({StockInfoConfig.class})  // 开启某个特定属性配置类实例化的方法之一
public class CommonConfig {

    /**
    * 密码加密器
    * BCryptPasswordEncoder使用SHA-256进行加密
    * @return 密码加密器
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 生成id生成器
     * @return id生成器
     */
    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1L, 2L);
    }
}
