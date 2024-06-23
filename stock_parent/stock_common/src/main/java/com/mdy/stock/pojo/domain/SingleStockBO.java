package com.mdy.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mdy
 * @date 2024-06-22 20:25
 * @description 个股分时数据
 */
@Data
public class SingleStockBO {
    /**
     * 股票编码
     */
    private String code;

    /**
     * 当前时间，精确到分钟
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    /**
     * 最高价格
     */
    private BigDecimal highPrice;
    /**
     * 最低价格
     */
    private BigDecimal lowPrice;
    /**
     * 股票名称
     */
    private String name;
    /**
     * 开盘价
     */
    private BigDecimal openPrice;
    /**
     * 前收盘价格
     */
    private BigDecimal preClosePrice;
    /**
     * 当前交易量
     */
    private BigDecimal tradeAmt;
    /**
     * 当前价格（最新价格）
     */
    private BigDecimal tradePrice;
    /**
     * 交易金额
     */
    private BigDecimal tradeVol;
}
