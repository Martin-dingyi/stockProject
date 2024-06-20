package com.mdy.stock.service;

import com.mdy.stock.pojo.domain.InnerMarketDomain;
import com.mdy.stock.pojo.domain.InnerSectorDomain;
import com.mdy.stock.viewObject.response.R;

import java.util.List;

/**
 * @author mdy
 * @date 2024-06-20 13:51
 * @description
 */
public interface StockService {

    /**
     * 获取所有国内大盘指数数据
     * @return
     */
    R<List<InnerMarketDomain>> getInnerIndexAll();


    R<List<InnerSectorDomain>> getInnerSectorAll();
}
