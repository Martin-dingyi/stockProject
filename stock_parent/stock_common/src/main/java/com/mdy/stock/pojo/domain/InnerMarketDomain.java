package com.mdy.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author mdy
 * @date 2024-06-12 14:12
 * @description 国内A股大盘指数信息实体类
 */

@Data
public class InnerMarketDomain {
    /**
     * 大盘编码
     */
    private String code;
    /**
     * 指数名称
     */
    private String name;
    /**
     * 前收盘点
     */
    private BigDecimal preClosePoint;
    /**
     * 开盘点
     */
    private BigDecimal openPoint;
    /**
     * 当前点
     */
    private BigDecimal curPoint;
    /**
     * 交易量
     */
    private Long tradeAmt;
    /**
     * 交易金额
     */
    private Long tradeVol;
    /**
     * 涨跌值
     */
    private BigDecimal upDown;
    /**
     * 涨幅
     */
    private BigDecimal rose;
    /**
     * 振幅
     */
    private BigDecimal amplitude;

    /**
     * 当前时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String curTime;


}
