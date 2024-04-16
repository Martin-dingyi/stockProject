package com.mdy.stock;

import com.mdy.stock.mapper.SysUserMapper;
import com.mdy.stock.service.UserService;
import com.mdy.stock.service.impl.UserServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author mdy
 * @date 2024-04-16 9:40
 * @description SpringBoot启动类
 */

@MapperScan("com.mdy.stock.mapper")
@SpringBootApplication
@EnableAspectJAutoProxy
public class BackendApp {
    public static void main(String[] args) {
        SpringApplication.run(BackendApp.class, args);
    }
}
