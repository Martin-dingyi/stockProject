package com.mdy.stock;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mdy
 * @date 2024-06-25 1:53
 * @description
 */
@SpringBootTest
public class PortionTest {

    @Test
    public void test() {
        List<Integer> all = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            all.add(i);
        }
        //将集合均等分，每份大小最多15个
        Lists.partition(all,15).forEach(ids->{
            System.out.println(ids);
        });
    }
}
