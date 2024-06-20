package com.mdy.stock.controller;

import com.mdy.stock.pojo.domain.InnerSectorDomain;
import com.mdy.stock.service.StockService;
import com.mdy.stock.pojo.domain.InnerMarketDomain;
import com.mdy.stock.viewObject.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
