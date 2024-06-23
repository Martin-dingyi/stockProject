package com.mdy.stock.controller;

import com.mdy.stock.pojo.domain.*;
import com.mdy.stock.service.StockService;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author mdy
 * @date 2024-06-20 13:42
 * @description 股票数据相关接口
 */

@RestController
@RequestMapping("/api/quot")
public class StockController {

    @Resource
    StockService stockService;

    /**
     * 获取全部国内大盘信息
     * @return R
     */
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketData() {
        return stockService.getInnerIndexAll();
    }

    /**
     * 获取十条国内板块信息
     * @return R
     */
    @GetMapping("/sector/all")
    public R<List<InnerSectorDomain>> getSectorData() {
        return stockService.getInnerSectorAll();
    }

    /**
     * 根据页码和页面大小获取股票涨幅榜数条数据
     * @param page 当前页
     * @param pageSize 页面大小
     * @return R
     */
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getStockPageInfo(@RequestParam(name = "page", required = false,
            defaultValue = "1") Integer page, @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        return stockService.getStockUpDownPageInfos(page, pageSize);
    }

    /**
     * 返回涨幅榜前四条数据，根据涨幅排列。
     * @return R
     */
    @GetMapping("/stock/increase")
    public R<List<StockUpdownDomain>> getStockIncrease() {
        return stockService.getUpDownIncreaseInfo();
    }

    /**
     * 返回最近交易时间内涨停和跌停的股票的数据，包括他们涨停或跌停的时间，以及涨跌停股票的数量
     * @return R
     */
    @GetMapping("/stock/updown/count")
    public R<Map> getStockUpDownCount() {
        return stockService.getStockUpDownCount();
    }

    /**
     * 根据当前页下载股票涨跌数据
     * @param page 当前页
     * @param pageSize 页大小
     * @param response servlet的http响应对象
     */
    @GetMapping("/stock/export")
    public void downloadStockUpDown(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            HttpServletResponse response) {
        stockService.downloadStockUpDown(page, pageSize, response);
    }

    /**
     * 获取今天或昨天沪深两市的每分钟总交易量
     * @return R
     */
    @GetMapping("/stock/tradeAmt")
    public R<Map> getStockTradeAmt() {
        return stockService.getStockTradeAmountForTodayAndYesterday();
    }

    /**
     * 获取涨跌区间计数
     * @return R
     */
    @GetMapping("/stock/updown")
    public R<Map<String, Object>> getStockUpDown() {
        return stockService.getStockUpDownIntervalCnt();
    }

    /**
     * 根据编码获取单一股票的最近的分时数据
     * @param code 股票编码
     * @return R
     */
    @GetMapping("/stock/screen/time-sharing")
    public R<List<SingleStock>> getStockMinuteDataByCode(@RequestParam(name = "code") String code) {
        return stockService.getStockMinuteDataByCode(code);
    }

    /**
     * 根据编码获取单一股票最近几天的日k线数据
     * @param code 股票编码
     * @return R
     */
    @GetMapping("/stock/screen/dkline")
    public R<List<StockDayBO>> getStockDayDataByCode(@RequestParam(name = "code") String code) {
        return stockService.getStockDayDataByCode(code);
    }
}
