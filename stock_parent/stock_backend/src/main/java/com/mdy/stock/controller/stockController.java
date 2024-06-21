package com.mdy.stock.controller;

import com.mdy.stock.pojo.domain.InnerSectorDomain;
import com.mdy.stock.pojo.domain.StockUpdownDomain;
import com.mdy.stock.service.StockService;
import com.mdy.stock.pojo.domain.InnerMarketDomain;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mdy
 * @date 2024-06-20 13:42
 * @description
 */

@RestController
@RequestMapping("/api/quot")
public class stockController {

    @Autowired
    StockService stockService;

    /**
     * 获取全部国内大盘信息
     * @return
     */
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketData() {
        return stockService.getInnerIndexAll();
    }

    /**
     * 获取十条国内板块信息
     * @return
     */
    @GetMapping("/sector/all")
    public R<List<InnerSectorDomain>> getSectorData() {
        return stockService.getInnerSectorAll();
    }

    /**
     * 根据页码和页面大小获取股票涨幅榜数条数据
     * @param page 当前页
     * @param pageSize 页面大小
     * @return
     */
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getStockPageInfo(@RequestParam(name = "page", required = false,
            defaultValue = "1") Integer page, @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        return stockService.getStockUpDownPageInfos(page, pageSize);
    }

    /**
     * 返回涨幅榜前四条数据，根据涨幅排列。
     * @return
     */
    @GetMapping("stock/increase")
    public R<List<StockUpdownDomain>> getStockIncrease() {
        return stockService.getUpDownIncreaseInfo();
    }


}
