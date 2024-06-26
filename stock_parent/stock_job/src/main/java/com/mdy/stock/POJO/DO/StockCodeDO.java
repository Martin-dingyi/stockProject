package com.mdy.stock.POJO.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mdy
 * @date 2024-06-25 2:16
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockCodeDO {

    /**
     * 交易码，即股票id
     */
    private String dm;
    /**
     * 股票名称
     */
    private String mc;
    /**
     * 股票所在交易所
     */
    private String jys;
}
