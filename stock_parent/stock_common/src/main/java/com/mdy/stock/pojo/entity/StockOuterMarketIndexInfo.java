package com.mdy.stock.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 外盘详情信息表
 * @TableName stock_outer_market_index_info
 */
@Data
public class StockOuterMarketIndexInfo implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 大盘编码
     */
    private String marketCode;

    /**
     * 大盘名称
     */
    private String name;

    /**
     * 大盘当前点
     */
    private BigDecimal curPoint;

    /**
     * 大盘涨跌值
     */
    private BigDecimal upDown;

    /**
     * 大盘涨幅
     */
    private BigDecimal rose;

    /**
     * 当前时间
     */
    @JsonFormat(pattern = "yyyyMMdd")
    private Date curTime;

    private static final long serialVersionUID = 1L;
}