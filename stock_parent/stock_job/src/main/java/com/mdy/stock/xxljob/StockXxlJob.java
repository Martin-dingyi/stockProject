package com.mdy.stock.xxljob;

import com.mdy.stock.service.StockTimerTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mdy
 * @date 2024-06-26 17:53
 * @description 配置xxj-job要执行的定时任务
 */
@Component
public class StockXxlJob {

    @Resource
    private StockTimerTaskService stockTimerTaskService;

    /**
     * 定时执行获取大盘指数数据
     */
    @XxlJob("getMarketInfo")
    public void getMarketInfo() throws Exception {
        stockTimerTaskService.getAndInsertInnerMarketInfo();
    }

    /**
     * 定时执行获取个股数据
     */
    @XxlJob("getStockInfo")
    public void getStockInfo() throws Exception {
        stockTimerTaskService.getAndInsertStockInfo();
    }
}
