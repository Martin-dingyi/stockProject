package com.mdy.stock;

import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.utils.IdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootTest
public class TestSomething {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    IdWorker idWorker;

    @Autowired
    StockInfoConfig stockInfoConfig;

    @Test
    public void testEncoding() {
        String password = "123456";
        System.out.println(passwordEncoder.encode(password));
    }

    @Test
    public void testSnowFlake() {
        System.out.println(idWorker.nextId());
    }

    @Test
    public void testConfigProperties() {
        System.out.println(Arrays.toString(stockInfoConfig.getInnerMarketId().toArray()));
    }
}
