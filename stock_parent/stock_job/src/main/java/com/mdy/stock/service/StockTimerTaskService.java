package com.mdy.stock.service;

/**
 * @author mdy
 * @date 2024-06-24 17:07
 * @description 股票定时采集数据服务
 */
public interface StockTimerTaskService {
    /**
     * 获取国内大盘的实时数据信息
     */
    void getAndInsectInnerMarketInfo();

    /**
     * 获取沪深两市个股的实时数据信息
     */
    void getStockInfo();
}
