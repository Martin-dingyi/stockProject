package com.mdy.stock;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

/**
 * @author mdy
 * @date 2024-06-12 17:29
 * @description 用于测试获取时间的工具joda
 */
public class TestJoda {
    @Test
    public void test() {
        DateTime dateTime = new DateTime();
        System.out.println(dateTime.dayOfWeek().getDateTime());
        System.out.println(dateTime.withHourOfDay(14).withMinuteOfHour(58).withSecondOfMinute(0).withMillisOfSecond(0));
    }
}
