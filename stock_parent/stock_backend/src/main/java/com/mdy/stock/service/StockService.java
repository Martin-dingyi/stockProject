package com.mdy.stock.service;

import com.mdy.stock.pojo.domain.InnerMarketDomain;
import com.mdy.stock.pojo.domain.InnerSectorDomain;
import com.mdy.stock.pojo.domain.StockUpdownDomain;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;

import java.util.List;
import java.util.Map;

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


    /**
     * 获取所有国内板块数据
     * @return
     */
    R<List<InnerSectorDomain>> getInnerSectorAll();

    /**
     * 根据分页数据获取涨幅榜信息，查询最新的数据。
     * @param page 当前页
     * @param pageSize 页大小
     * @return
     */
    R<PageResult<StockUpdownDomain>> getStockUpDownPageInfos(Integer page, Integer pageSize);

    /**
     * 获取最新的前四条涨幅榜数据，根据涨幅排序
     * @return
     */

    R<List<StockUpdownDomain>> getUpDownIncreaseInfo();

    R<Map> getStockUpDownCount();
}
