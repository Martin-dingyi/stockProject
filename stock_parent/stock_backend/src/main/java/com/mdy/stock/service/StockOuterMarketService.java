package com.mdy.stock.service;

import com.mdy.stock.pojo.entity.StockOuterMarketIndexInfo;
import com.mdy.stock.viewObject.response.R;

import java.util.List;

/**
 * @author mdy
 * @date 2024-06-30 21:32
 * @description
 */
public interface StockOuterMarketService {


    /**
     * 获取四条国外大盘数据，按照时间和大盘点数降序排序。
     * @return R
     */
    R<List<StockOuterMarketIndexInfo>> getOuterMarketIndexInfo();
}
