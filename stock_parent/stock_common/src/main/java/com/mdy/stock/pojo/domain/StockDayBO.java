package com.mdy.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mdy
 * @date 2024-06-23 18:32
 * @description 描述一支股票一天的信息
 */
@Data
public class StockDayBO {
    /**
     * 股票编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 前收盘价
     */
    private BigDecimal preClosePrice;
    /**
     * 开盘价
     */
    private BigDecimal openPrice;
    /**
     * 最高价（指收盘时记录的最高价，如果当天未收盘，则显示最新数据）
     */
    private BigDecimal highPrice;
    /**
     * 最低价格（指收盘时记录的最低价，如果当天未收盘，则显示最新数据）
     */
    private BigDecimal lowPrice;
    /**
     * 当前收盘价（指收盘时的价格，如果当天未收盘，则显示最新cur_price）
     */
    private BigDecimal closePrice;
    /**
     * 交易量(指收盘时的交易量，如果当天未收盘，则显示最新数据)
     */
    private BigDecimal tradeAmt;
    /**
     * 交易金额（指收盘时记录交易量，如果当天未收盘，则显示最新数据）
     */
    private BigDecimal tradeVol;
    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date date;
}
