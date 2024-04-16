package com.mdy.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class TestSomething {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testEncoding() {
        String password = "123456";
        System.out.println(passwordEncoder.encode(password));
    }
}
