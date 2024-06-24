package com.mdy.stock.POJO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mdy
 * @date 2024-06-25 2:08
 * @description
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InnerStockDO {
    /**
     * 昨日收盘价
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
     * 最低价
     */
    private BigDecimal l;
    /**
     * 最低价
     */
    private BigDecimal h;
    /**
     * 成交量
     */
    private BigDecimal v;
    /**
     * 成交额
     */
    private BigDecimal cje;
    /**
     * 当前日期
     */
    private String t;
}
