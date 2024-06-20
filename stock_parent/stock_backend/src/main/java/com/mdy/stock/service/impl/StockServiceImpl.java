package com.mdy.stock.service.impl;

import com.mdy.stock.mapper.StockMarketIndexInfoMapper;
import com.mdy.stock.pojo.domain.InnerSectorDomain;
import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.service.StockService;
import com.mdy.stock.utils.DateTimeUtil;
import com.mdy.stock.pojo.domain.InnerMarketDomain;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.ResponseCode;
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
    public R<List<InnerMarketDomain>> getInnerIndexAll() {
        // 1.获取最近交易时间
        Date lastTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        // ！！！！mock数据，暂时使用，后期删除。
        // DateTime.parse作用：将字符串转化为DateTime类型
        lastTime = DateTime.parse("2022-01-02 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        // 2.获取所有国内大盘编码
        List<String> innerMarketCodes = stockInfoConfig.getInner();
        // 3.根据最近时间和大盘编码查询数据库
        List<InnerMarketDomain> infos = stockMarketIndexInfoMapper.getInnerMarketInfo(lastTime, innerMarketCodes);
        return R.ok(infos);
    }

    /**
     * 获取最新国内板块的十条数据，按照交易量降序排列
     * @return
     */
    @Override
    public R<List<InnerSectorDomain>> getInnerSectorAll() {
        // 1.获取最近交易时间
        Date lastTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();

        // ！！！！mock数据，暂时使用，后期删除。
        // DateTime.parse作用：将字符串转化为DateTime类型
        lastTime = DateTime.parse("2021-12-21 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        // 2.根据最新交易时间查询十条数据
        List<InnerSectorDomain> infos = stockMarketIndexInfoMapper.getInnerMarketSectorInfo(lastTime);
        // 3.若无数据，则报错
        if (infos == null || infos.isEmpty()) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(infos);
    }
}
