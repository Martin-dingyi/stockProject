package com.mdy.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mdy
 * @date 2024-07-01 3:19
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockBusinessBO {
    /**
     * 股票编码
     */
    private String code;

    /**
     * 行业，也就是行业板块名称
     */
    private String trade;

    /**
     * 公司主营业务
     */
    private String business;

    /**
     * 公司名称
     */
    private String name;
}
