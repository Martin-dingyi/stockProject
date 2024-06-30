package com.mdy.stock.controller;

import com.mdy.stock.pojo.entity.StockOuterMarketIndexInfo;
import com.mdy.stock.service.StockOuterMarketService;
import com.mdy.stock.viewObject.response.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mdy
 * @date 2024-06-30 20:21
 * @description 外盘相关API
 */

@RestController
@RequestMapping("/api/quot/external")
public class OuterStockController {

    @Resource
    private StockOuterMarketService stockOuterMarketService;

    /**
     * 获取四条国外大盘数据，按照时间和大盘点数降序排序。
     * @return R
     */
    @GetMapping("/index")
    public R<List<StockOuterMarketIndexInfo>> getOuterMarketIndexInfo() {
        return stockOuterMarketService.getOuterMarketIndexInfo();
    }
}
