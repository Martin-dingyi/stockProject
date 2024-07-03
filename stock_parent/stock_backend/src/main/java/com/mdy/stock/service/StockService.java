package com.mdy.stock.service;

import com.mdy.stock.pojo.domain.*;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author mdy
 * @date 2024-06-20 13:51
 * @description 股票数据相关服务接口
 */
public interface StockService {

    /**
     * 获取所有国内大盘指数数据
     * @return
     */
    R<List<InnerMarketBO>> getInnerIndexAll();

    /**
     * 获取所有国内板块数据
     * @return
     */
    R<List<InnerSectorBO>> getInnerSectorAll();

    /**
     * 根据分页数据获取涨幅榜信息，查询最新的数据。
     * @param page 当前页
     * @param pageSize 页大小
     * @return
     */
    R<PageResult<StockUpDownBO>> getStockUpDownPageInfos(Integer page, Integer pageSize);

    /**
     * 获取最近时间内的前四条涨幅榜数据，根据涨幅排序
     * @return
     */
    R<List<StockUpDownBO>> getUpDownIncreaseInfo();

    /**
     * 获得股票涨跌停数据计数统计
     * @return
     */
    R<Map> getStockUpDownCount();

    /**
     * 将涨幅榜分页数据根据excel的格式写入网络数据流
     * @param page 当前页
     * @param pageSize 页大小
     * @param response http响应对象
     */
    void downloadStockUpDown(Integer page, Integer pageSize, HttpServletResponse response);

    /**
     * 获取今天或昨天沪深两市的每分钟总交易量
     * @return
     */
    R<Map> getStockTradeAmountForTodayAndYesterday();

    /**
     * 获取涨跌区间计数
     * @return R
     */
    R<Map<String, Object>> getStockUpDownIntervalCnt();

    /**
     * 根据编码获取所有个股股票的最近的分时数据
     * @param code 股票编码
     * @return R
     */
    R<List<InnerStockBO>> listStockPerMinuteByCode(String code);

    /**
     * 根据编码获取单一股票最近几天的日k线数据
     * @param code 股票编码
     * @return R
     */
    R<List<StockDayBO>> getStockDayDataByCode(String code);


    /**
     * 根据编码模糊查询相关股票
     * @param code 模糊code
     * @return 返回查询到的股票的code和name
     */
    R<List<InnerMarketBO>> getRelatedStockInfo(String code);

    /**
     * 根据编码获取个股商业信息
     * @param code 编码
     * @return R
     */
    R<StockBusinessBO> getStockBusinessInfoByCode(String code);

    /**
     * 根据股票编码获取股票周k线数据
     *
     * @param code 编码
     * @return R
     */
    R<List<StockWeeklyBO>> getStockWeeklyByCode(String code);

    /**
     * 根据编码获取单一股票的最近的分时数据
     * @param code 股票编码
     * @return R
     */
    R<InnerStockBO> getStockDetailByCode(String code);
}
