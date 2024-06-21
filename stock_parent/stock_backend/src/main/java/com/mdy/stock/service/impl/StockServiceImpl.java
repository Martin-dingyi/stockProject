package com.mdy.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mdy.stock.mapper.StockMarketIndexInfoMapper;
import com.mdy.stock.mapper.StockRtInfoMapper;
import com.mdy.stock.pojo.domain.InnerSectorDomain;
import com.mdy.stock.pojo.domain.StockUpdownDomain;
import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.service.StockService;
import com.mdy.stock.utils.DateTimeUtil;
import com.mdy.stock.pojo.domain.InnerMarketDomain;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.ResponseCode;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;

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
        List<InnerSectorDomain> innerMarketSectorInfos = stockMarketIndexInfoMapper.getInnerMarketSectorInfo(lastTime);
        // 3.若无数据，则报错
        if (CollectionUtils.isEmpty(innerMarketSectorInfos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        return R.ok(innerMarketSectorInfos);
    }

    @Override
    public R<PageResult<StockUpdownDomain>> getStockUpDownPageInfos(Integer page, Integer pageSize) {
        // 1.设置pageHelper分页参数
        PageHelper.startPage(page, pageSize);
        // 2.根据最新交易时间查询涨幅榜数据
        Date lastTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();

        // ！！！！mock数据，暂时使用，后期删除。
        // DateTime.parse作用：将字符串转化为DateTime类型
        lastTime = DateTime.parse("2022-06-07 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockUpdownDomain> stockUpDownInfos = stockRtInfoMapper.findAll(lastTime);
        if (CollectionUtils.isEmpty(stockUpDownInfos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        // 3.将数据加载到pageInfo中，再通过pageInfo生成pageResult
        PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(stockUpDownInfos);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
        return R.ok(pageResult);
    }

    @Override
    public R<List<StockUpdownDomain>> getUpDownIncreaseInfo() {
        // 1.获取最近交易时间
        Date lastTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();

        // ！！！！mock数据，暂时使用，后期删除。
        // DateTime.parse作用：将字符串转化为DateTime类型
        lastTime = DateTime.parse("2022-06-07 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockUpdownDomain> stockUpDownInfos = stockRtInfoMapper.findFourUpDownData(lastTime);
        if (CollectionUtils.isEmpty(stockUpDownInfos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        return R.ok(stockUpDownInfos);
    }

    @Override
    public R<Map> getStockUpDownCount() {
        // 1.获取最近交易时间
        Date lastTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        // ！！！！mock数据，暂时使用，后期删除。
        lastTime = DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        // 2.获取最近交易时间下的开盘时间
        Date openTime = DateTimeUtil.getOpenDate(DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))).toDate();
        // 3.查询该时间段内涨跌停的数据
        List<Map> upDataList = stockRtInfoMapper.findUpDownCount(openTime, lastTime, 1);
        List<Map> downDataList = stockRtInfoMapper.findUpDownCount(openTime, lastTime, 0);
        // 4.制作返回数据
        Map<String, List<Map>> stockUpDownCount = new HashMap<>();
        stockUpDownCount.put("upList", upDataList);
        stockUpDownCount.put("downList", downDataList);
        return R.ok(stockUpDownCount);
    }

    /**
     * 根据当前页下载股票涨跌数据
     * @param page 当前页
     * @param pageSize 页大小
     * @param response servlet的http响应对象
     */
    @Override
    public void downloadStockUpDown(Integer page, Integer pageSize, HttpServletResponse response) {
        List<StockUpdownDomain> stockUpDownInfos = this.getStockUpDownPageInfos(page, pageSize).getData().getRows();
        // 设置响应的文件格式和编码格式
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            // 设置下载的默认的文件名
            String fileName = URLEncoder.encode("股票涨跌数据", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class).sheet("股票涨跌信息").doWrite(stockUpDownInfos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
