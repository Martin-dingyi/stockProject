package com.mdy.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mdy.stock.mapper.StockMarketIndexInfoMapper;
import com.mdy.stock.mapper.StockRtInfoMapper;
import com.mdy.stock.pojo.domain.*;
import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.service.StockService;
import com.mdy.stock.utils.DateTimeUtil;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author mdy
 * @date 2024-06-20 13:52
 * @description
 */
@Service
@Slf4j
public class StockServiceImpl implements StockService {
    @Resource
    StockInfoConfig stockInfoConfig;

    @Resource
    StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Resource
    private StockRtInfoMapper stockRtInfoMapper;

    @Resource
    private Cache<String, R> caffeineCache;

    /**
     * 获取两个国内大盘指数信息
     * @return R
     */
    @Override
    public R<List<InnerMarketDomain>> getInnerIndexAll() {
        R<List<InnerMarketDomain>> data = caffeineCache.get("marketInfo", key -> {
            // 如果缓存不存在，向数据库查询。
            // 1.获取最近交易时间
            Date lastTime = DateTimeUtil.getLastValidDate(DateTime.now().minusMinutes(1)).toDate();
            // 2.获取所有国内大盘编码
            List<String> innerMarketCodes = stockInfoConfig.getInnerMarketId();
            // 3.根据最近时间和大盘编码查询数据库
            List<InnerMarketDomain> infos = stockMarketIndexInfoMapper.getInnerMarketInfo(lastTime, innerMarketCodes);
            return R.ok(infos);
        });
        return data;
    }

    /**
     * 获取最新国内板块的十条数据，按照交易量降序排列
     * @return R
     */
    @Override
    public R<List<InnerSectorDomain>> getInnerSectorAll() {
        // 1.获取最近交易时间
        Date lastTime = DateTimeUtil.getLastValidDate(DateTime.now().minusMinutes(1)).toDate();
        // 2.根据最新交易时间查询十条数据
        List<InnerSectorDomain> innerMarketSectorInfos = stockMarketIndexInfoMapper.getInnerMarketSectorInfo(lastTime);
        // 3.若无数据，则报错
        if (CollectionUtils.isEmpty(innerMarketSectorInfos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        return R.ok(innerMarketSectorInfos);
    }

    /**
     * 获取涨跌幅的分页数据
     * @param page 当前页
     * @param pageSize 页大小
     * @return R
     */
    @Override
    public R<PageResult<StockUpdownDomain>> getStockUpDownPageInfos(Integer page, Integer pageSize) {
        // 1.设置pageHelper分页参数
        PageHelper.startPage(page, pageSize);
        // 2.根据最新交易时间查询涨幅榜数据
        Date lastTime = DateTimeUtil.getLastValidDate(DateTime.now().minusMinutes(1)).toDate();
        List<StockUpdownDomain> stockUpDownInfos = stockRtInfoMapper.findAll(lastTime);
        if (CollectionUtils.isEmpty(stockUpDownInfos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        // 3.将数据加载到pageInfo中，再通过pageInfo生成pageResult
        PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(stockUpDownInfos);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
        return R.ok(pageResult);
    }

    /**
     * 获取所有涨跌幅数据
     * @return R
     */
    @Override
    public R<List<StockUpdownDomain>> getUpDownIncreaseInfo() {
        // 1.获取最近交易时间
        Date lastTime = DateTimeUtil.getLastValidDate(DateTime.now().minusMinutes(1)).toDate();
        List<StockUpdownDomain> stockUpDownInfos = stockRtInfoMapper.findFourUpDownData(lastTime);
        if (CollectionUtils.isEmpty(stockUpDownInfos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        return R.ok(stockUpDownInfos);
    }

    /**
     * 获取各个股票涨跌停的次数
     * @return R
     */
    @Override
    public R<Map> getStockUpDownCount() {
        // 1.获取最近交易时间
        Date lastTime = DateTimeUtil.getLastValidDate(DateTime.now().minusMinutes(1)).toDate();
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
     * 根据当前页和页大小下载股票涨跌数据，输出excel表格
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

    /**
     * 获取今天或昨天沪深两市的每分钟总交易量
     * @return R
     */
    @Override
    public R<Map> getStockTradeAmountForTodayAndYesterday() {
        // 1.获取今天和昨天的日期
        DateTime curTime = DateTimeUtil.getLastValidDate(DateTime.now().minusMinutes(1));
        DateTime yesterdayTime = DateTimeUtil.getPreDateTime(curTime);
        // 2.获取国内大盘编码
        List<String> innerStockCodes = stockInfoConfig.getInnerMarketId();
        // 3.查询今明两天的每分钟交易量总额
        List<Map> amtList =  stockMarketIndexInfoMapper.getStockTradeAmount(curTime.toDate(), curTime.plusDays(1).toDate(), innerStockCodes);
        List<Map> yesAmtList = stockMarketIndexInfoMapper.getStockTradeAmount(yesterdayTime.toDate(), curTime.toDate(), innerStockCodes);
        // 异常处理：如果没查到数据，则返回一个空集合
        if (CollectionUtils.isEmpty(amtList)) {
            amtList = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(yesAmtList)) {
            yesAmtList = new ArrayList<>();
        }
        // 4.组装并返回数据
        Map<String, List<Map>> infoMap = new HashMap<>();
        infoMap.put("amtList", amtList);
        infoMap.put("yesAmtList", yesAmtList);
        return R.ok(infoMap);
    }

    /**
     * 获取涨跌区间计数
     * @return R
     */
    @Override
    public R<Map<String, Object>> getStockUpDownIntervalCnt() {
        // 1.获取最近交易时间
        DateTime lastTimeOfDateTime = DateTimeUtil.getLastValidDate(DateTime.now().minusMinutes(1));
        Date lastTime = lastTimeOfDateTime.toDate();

        // 2.查询数据
        List<Map<String, Object>> intervalCnt = stockRtInfoMapper.findUpDownInterValCnt(lastTime);

        // 3.按顺序将数据载入相应区间
        // 获取固定的区间列表
        List<String> intervalList = stockInfoConfig.getIntervalList();
        List<Map<String, Object>> intervalCntLinkedList = new ArrayList<>();
        for (String interval : intervalList) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", interval);
            map.put("count", 0);
            intervalCntLinkedList.add(map);
            for (Map<String, Object> mapInfo : intervalCnt) {
                if (mapInfo.containsValue(interval)) {
                    map.put("count", mapInfo.get("count"));
                    break;
                }
            }
        }

        // 4.封装并返回数据
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("time", lastTimeOfDateTime.toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        infoMap.put("infos", intervalCntLinkedList);
        return R.ok(infoMap);
    }

    /**
     * 根据编码获取单一股票的最近的分时数据
     * @param code 股票编码
     * @return R
     */
    @Override
    public R<List<SingleStock>> getStockMinuteDataByCode(String code) {
        R<List<SingleStock>> data = caffeineCache.get("stockInfo", key -> {
            // 如果缓存不存在，向数据库查询。
            // 1.获取最近有效交易时间
            DateTime lastTimeOfDateTime = DateTimeUtil.getLastValidDate(DateTime.now().minusMinutes(1));
            Date lastTime = lastTimeOfDateTime.toDate();
            // 2.获取最近有效时间下的开盘时间
            Date openTime = DateTimeUtil.getOpenDate(lastTimeOfDateTime).toDate();
            // 3.根据股票编码、开盘时间和最后时间查询单一股票分时数据
            List<SingleStock> minuteStockList = stockRtInfoMapper.findSingleStockMinuteDataByCode(openTime, lastTime, code);
            return R.ok(minuteStockList);
        });
        return data;
    }

    /**
     * 根据编码获取单一股票最近几天的日k线数据
     * @param code 股票编码
     * @return R
     */
    @Override
    public R<List<StockDayBO>> getStockDayDataByCode(String code) {
        R<List<StockDayBO>> data = caffeineCache.get("stockInfoDay", key -> {
            // 如果缓存不存在，向数据库查询。
            // 1.获取最近有效交易时间
            DateTime lastTimeOfDateTime = DateTimeUtil.getLastValidDate(DateTime.now().minusMinutes(1));
            Date lastTime = lastTimeOfDateTime.toDate();
            // 2.获取最近交易时间前一个月的日期
            Date lastTimePreMouth = lastTimeOfDateTime.minusMonths(1).toDate();
            // 3.根据开始和结束日期，查出这个期间编码为code的股票的所有日k线数据
            List<StockDayBO> stockDayBOList = stockRtInfoMapper.findStockKDayDataByCode(lastTimePreMouth, lastTime, code);
            return R.ok(stockDayBOList);
        });
        return data;
    }

}
