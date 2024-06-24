package com.mdy.stock.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 国内大盘数据详情表
 * @TableName stock_market_index_info
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockMarketIndexInfo implements Serializable {
    /**
     * 主键字段（无业务意义）
     */
    private Long id;

    /**
     * 大盘编码
     */
    private String marketCode;

    /**
     * 指数名称
     */
    private String marketName;

    /**
     * 前收盘点数
     */
    private BigDecimal preClosePoint;

    /**
     * 开盘点数
     */
    private BigDecimal openPoint;

    /**
     * 当前点数
     */
    private BigDecimal curPoint;

    /**
     * 最低点数
     */
    private BigDecimal minPoint;

    /**
     * 最高点数
     */
    private BigDecimal maxPoint;

    /**
     * 成交量(手)
     */
    private Long tradeAmount;

    /**
     * 成交金额（元）
     */
    private BigDecimal tradeVolume;

    /**
     * 当前时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date curTime;

    private static final long serialVersionUID = 1L;
}