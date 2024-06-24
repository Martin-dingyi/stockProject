package com.mdy.stock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mdy
 * @date 2024-06-24 0:22
 * @description
 */

@MapperScan("com.mdy.stock.mapper")
@SpringBootApplication
public class JobApp {
    public static void main(String[] args) {
        SpringApplication.run(JobApp.class, args);
    }
}
