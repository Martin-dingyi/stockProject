package com.mdy.stock.xxljob;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @author mdy
 * @date 2024-06-26 17:53
 * @description
 */
@Component
public class SampleXxlJob {
    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("我的第一个定时任务")
    public void demoJobHandler() throws Exception {
        System.out.println("hello xxljob.....");
    }
}
