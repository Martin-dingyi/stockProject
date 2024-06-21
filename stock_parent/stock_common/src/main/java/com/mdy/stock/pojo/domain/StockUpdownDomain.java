package com.mdy.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author by itheima
 * @Date 2022/2/28
 * @Description 股票涨跌信息
 */
@Data
public class StockUpdownDomain {
    private String code;

    private String name;

    private BigDecimal preClosePrice;

    private BigDecimal tradePrice;

    private BigDecimal increase;

    private BigDecimal upDown;

    private BigDecimal amplitude;

    private Long tradeAmt;

    private BigDecimal tradeVol;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curDate;
}
