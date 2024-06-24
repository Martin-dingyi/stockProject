package com.mdy.stock.POJO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mdy
 * @date 2024-06-24 17:13
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InnerStockDataDO {
    /**
     * 前收盘价
     */
    private BigDecimal yc;
    /**
     * 开盘价
     */
    private BigDecimal o;
    /**
     * 当前价
     */
    private BigDecimal p;
    /**
     * 最高价
     */
    private BigDecimal h;
    /**
     * 最低价
     */
    private BigDecimal l;
    /**
     * 涨跌额
     */
    private BigDecimal ud;
    /**
     * 涨跌幅
     */
    private BigDecimal pc;
    /**
     * 成交量
     */
    private BigDecimal v;
    /**
     * 成交额
     */
    private BigDecimal cje;
    /**
     * 当前时间
     */
    String t;
}
