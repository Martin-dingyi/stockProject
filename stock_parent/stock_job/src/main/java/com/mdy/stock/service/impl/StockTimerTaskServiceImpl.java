package com.mdy.stock.service.impl;

import com.mdy.stock.POJO.InnerStockDataDO;
import com.mdy.stock.mapper.StockOuterMarketIndexInfoMapper;
import com.mdy.stock.pojo.entity.StockMarketIndexInfo;
import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.service.StockTimerTaskService;
import com.mdy.stock.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * @author mdy
 * @date 2024-06-24 17:09
 * @description
 */

@Service
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {

    @Resource
    StockInfoConfig stockInfoConfig;

    @Resource
    RestTemplate restTemplate;

    @Resource
    IdWorker idWorker;

    @Resource
    StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;

    private String getMarketNameByCode(String code) {
        switch (code) {
            case "sh000001": return "上证指数";
            case "sz399001": return "深圳指数";
            default: return "未知国内指数";
        }
    }

    /**
     * 获取国内大盘的实时数据信息
     */
    @Override
    public void getInnerMarketInfo() {
        // 组装请求大盘数据的url
        List<String> marketCodeList = stockInfoConfig.getInnerMarketId();
        List<StockMarketIndexInfo> stockMarketIndexInfos = new ArrayList<>();

        // 请求数据，并将获取的数据封装到数据库表实体类StockMarketIndexInfo中
        for (String code : marketCodeList) {
            String marketUrl = stockInfoConfig.getMarketUrl() + code + "/" + stockInfoConfig.getLicence();
            InnerStockDataDO innerStockDataDO = restTemplate.getForObject(marketUrl, InnerStockDataDO.class);
            if (innerStockDataDO == null) {
                log.error("当前时间点：{} 采集数据失败。可能是因为licence受限，或者第三方的URL改变", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }
            log.info("当前时间点：{} 采集到的数据：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), innerStockDataDO);
            // 将日期字符串转换为date类型
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Objects.requireNonNull(innerStockDataDO).getT());
            } catch (ParseException parseException) {
                log.error("当前时间点：{} date数据转换失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }
            // 导入数据到实体类
            StockMarketIndexInfo stockMarketIndexInfo = new StockMarketIndexInfo();
            stockMarketIndexInfo.setId(idWorker.nextId());
            stockMarketIndexInfo.setMarketCode(code);
            stockMarketIndexInfo.setMarketName(getMarketNameByCode(code));
            stockMarketIndexInfo.setCurTime(date);
            stockMarketIndexInfo.setPreClosePoint(Objects.requireNonNull(innerStockDataDO).getYc());
            stockMarketIndexInfo.setCurPoint(innerStockDataDO.getP());
            stockMarketIndexInfo.setOpenPoint(innerStockDataDO.getO());
            stockMarketIndexInfo.setMaxPoint(innerStockDataDO.getH());
            stockMarketIndexInfo.setMinPoint(innerStockDataDO.getL());
            stockMarketIndexInfo.setTradeAmount(innerStockDataDO.getV().longValue());
            stockMarketIndexInfo.setTradeVolume(innerStockDataDO.getCje());
            stockMarketIndexInfos.add(stockMarketIndexInfo);
        }
        // 向数据库批量导入大盘指数信息
        stockOuterMarketIndexInfoMapper.insertStockInfosPatch(stockMarketIndexInfos);
    }
}
