package com.mdy.stock.viewObject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author mdy
 * @date 2024-06-12 14:12
 * @description 国内A股大盘信息实体类
 */

@Data
public class InnerMarketDomain {
    /**
     * 大盘编码
     */
    private String code;
    /**
     * 当前点
     */
    private BigDecimal curPoint;
    /**
     * 当前时间
     */
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm")
    private String curTime;
    /**
     * 指数名称
     */
    private String name;
    /**
     * 开盘点
     */
    private BigDecimal openPoint;
    /**
     * 前收盘点
     */
    private BigDecimal preClosePoint;
    /**
     * 涨幅
     */
    private BigDecimal rose;
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
     * 振幅
     */
    private BigDecimal amplitude;

}
