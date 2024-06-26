package com.mdy.stock;

import com.mdy.stock.mapper.StockRtInfoMapper;
import com.mdy.stock.pojo.entity.StockRtInfo;
import com.mdy.stock.pojo.valueObject.StockInfoConfig;
import com.mdy.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mdy
 * @date 2024-06-24 16:51
 * @description
 */
@SpringBootTest
public class StockApiTest {

    @Autowired
    StockInfoConfig stockInfoConfig;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    StockTimerTaskService stockTimerTaskService;

    @Test
    public void test2() {
        stockTimerTaskService.getAndInsertInnerMarketInfo();
    }

    @Test
    public void test3() throws InterruptedException {
        stockTimerTaskService.getAndInsertStockInfo();
        Thread.sleep(3000);
    }

    @Test
    public void sinaStockApiTest() {
        // 定义采集的url接口
        String url = "http://hq.sinajs.cn/list=sh000001,sz399001";
        // 调用restTemplate采集数据
        // 组装请求对象
        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer", "https://finance.sina.com.cn/stock/");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        // resetTemplate发起请求
        String resString = restTemplate.postForObject(url, entity, String.class);
        System.out.println(resString);
    }

    @Test
    public void regExrTest() {
        String target = "var hq_str_sz399001=\"深证成指,12101.371,12172.911,11972.023,12205.097,11971.334,0.000,0.000,47857870369,524892592190.995,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,2022-04-07,15:00:03,00\"";
        String reg = "var hq_str_(.+)=\"(.+)\"";
        // 编译表达式,获取编译对象
        Pattern pattern = Pattern.compile(reg);
        // 匹配字符串
        Matcher matcher = pattern.matcher(target);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        } else {
            System.out.println("解析匹配失敗");
        }
    }


    @Autowired
    StockRtInfoMapper stockRtInfoMapper;
    @Test
    public void stringJoinTest() {
//        List<String> marketCodeList = stockInfoConfig.getInnerMarketId();
//        String url = stockInfoConfig.getInnerMarketUrl() + String.join(",", marketCodeList);
//        System.out.println(url);

        List<StockRtInfo> stockRtInfos = new ArrayList<>();
        // 获取个股编码列表
        List<String> stockCodeList = stockRtInfoMapper.findStockCodeList();
        // 处理个股编码
        for (int i = 0; i < stockCodeList.size(); i++) {
            String code = (stockCodeList.get(i).startsWith("00") ? "sz" : "sh") + stockCodeList.get(i);
            stockCodeList.set(i, code);
        }
        System.out.println(stockCodeList);
    }
}
