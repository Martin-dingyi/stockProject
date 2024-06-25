package com.mdy.stock.mq;

import com.github.benmanes.caffeine.cache.Cache;
import com.mdy.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @author mdy
 * @date 2024-06-26 0:28
 * @description RabbitMQ监听器
 */

@Component
@Slf4j
public class jobQueueListener {

    @Resource
    private Cache<String, Object> caffeineCache;

    @Resource
    private StockService stockService;

    /**
     *
     * @param date
     */
    @RabbitListener(queues = "innerMarketQueue")
    public void listenInnerMarketQueue(Date date) {
        log.info("接受到来自JobAPP的消息");
        // 获取时间毫秒差值
        long diffTime = DateTime.now().getMillis() - new DateTime(date).getMillis();
        // 超过一分钟告警
        if (diffTime > 60000) {
            log.error("采集国内大盘时间点：{},同步超时：{}ms",new DateTime(date).toString("yyyy-MM-dd HH:mm:ss"),diffTime);
        }
        // 将缓存键置为无效
        caffeineCache.invalidate("marketInfo");
    }

    /**
     *
     * @param date
     */
    @RabbitListener(queues = "innerStockQueue")
    public void listenInnerStockQueue(Date date) {
        //获取时间毫秒差值
        long diffTime = DateTime.now().getMillis() - new DateTime(date).getMillis();
        //超过一分钟告警
        if (diffTime > 60000) {
            log.error("采集国内大盘时间点：{},同步超时：{}ms",new DateTime(date).toString("yyyy-MM-dd HH:mm:ss"),diffTime);
        }
        // 将缓存键置为无效
        caffeineCache.invalidate("stockInfo");
        caffeineCache.invalidate("stockInfoDay");
    }
}
