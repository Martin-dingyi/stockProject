package com.mdy.stock.pojo.valueObject;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author mdy
 * @date 2024-06-20 11:27
 * @description 加载配置在yml文件中属性的配置类。
 */

@Data
@ConfigurationProperties(prefix = "stock")
public class StockInfoConfig {

    // A股大盘id信息
    private List<String> innerMarketId;

    // 外盘id信息
    private List<String> outerMarketId;

    // 固定涨跌区别列表
    private List<String> intervalList;

    // 大盘数据公共url
    private String marketUrl;

    // 板块数据公共url
    private String blockUrl;

    // 麦蕊智数的licence证书
    private String licence;
}
