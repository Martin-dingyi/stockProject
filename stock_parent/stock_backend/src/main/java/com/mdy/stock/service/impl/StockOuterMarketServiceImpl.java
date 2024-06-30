package com.mdy.stock.service.impl;

import com.mdy.stock.mapper.StockOuterMarketIndexInfoMapper;
import com.mdy.stock.pojo.entity.StockOuterMarketIndexInfo;
import com.mdy.stock.service.StockOuterMarketService;
import com.mdy.stock.utils.DateTimeUtil;
import com.mdy.stock.viewObject.response.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mdy
 * @date 2024-06-30 21:37
 * @description
 */
@Service
public class StockOuterMarketServiceImpl implements StockOuterMarketService {

    @Resource
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;

    /**
     * 获取四条国外大盘数据，按照时间和大盘点数降序排序。
     * @return R
     */
    @Override
    public R<List<StockOuterMarketIndexInfo>> getOuterMarketIndexInfo() {
        Date lastTime = DateTimeUtil.getLastValidDate(DateTime.now().minusMinutes(1)).toDate();
        // Todo: mock数据
        lastTime = DateTime.parse("2021-12-01 10:57:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        return R.ok(stockOuterMarketIndexInfoMapper.findOuterMarketIndexInfo(lastTime));
    }
}
