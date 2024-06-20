package com.mdy.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mdy
 * @date 2024-06-20 20:46
 * @description
 */
@Data
public class InnerSectorDomain {
    /**
     * 平均价格
     */
    private BigDecimal avgPrice;
    /**
     * 板块编码
     */
    private String code;
    /**
     * 公司数量
     */
    private Integer companyNum;
    /**
     * 当前日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String curDate;
    /**
     * 板块名称
     */
    private String name;
    /**
     * 交易量
     */
    private Long tradeAmt;
    /**
     * 交易总金额
     */
    private BigDecimal tradeVol;
    /**
     * 涨跌率
     */
    private BigDecimal updownRate;
}
