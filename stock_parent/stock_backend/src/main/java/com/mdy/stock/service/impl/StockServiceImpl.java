package com.mdy.stock.service.impl;

import com.mdy.stock.mapper.StockMarketIndexInfoMapper;
import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.service.StockService;
import com.mdy.stock.utils.DateTimeUtil;
import com.mdy.stock.pojo.domain.InnerMarketDomain;
import com.mdy.stock.viewObject.response.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author mdy
 * @date 2024-06-20 13:52
 * @description
 */
@Service
public class StockServiceImpl implements StockService {
    @Autowired
    StockInfoConfig stockInfoConfig;

    @Autowired
    StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    /**
     * 获取最近交易时间，从配置文件中获取所有国内大盘id，根据这两个数据查询国内大盘信息
     * @return
     */
    @Override
    public R<List<InnerMarketDomain>> getInnerMarketData() {
        // 1.获取最近交易时间
        Date lastTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        // mock数据，暂时使用，后期删除。
        // DateTime.parse作用：将字符串转化为DateTime类型
        lastTime = DateTime.parse("2022-01-02 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        // 2.获取所有国内大盘编码
        List<String> innerMarketCodes = stockInfoConfig.getInner();
        // 3.根据最近时间和大盘编码查询数据库
        List<InnerMarketDomain> list = stockMarketIndexInfoMapper.getInnerMarketInfo(lastTime, innerMarketCodes);
        return R.ok(list);
    }
}
