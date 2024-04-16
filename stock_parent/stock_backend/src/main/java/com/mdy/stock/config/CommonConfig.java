package com.mdy.stock.config;

import com.mdy.stock.utils.IdWorker;
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
public class CommonConfig {

    /*
    * 密码加密器
    * BCryptPasswordEncoder使用SHA-256进行加密
    * @return
    * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1L, 2L);
    }
}
