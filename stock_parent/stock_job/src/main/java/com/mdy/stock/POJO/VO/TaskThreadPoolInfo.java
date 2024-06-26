package com.mdy.stock.POJO.VO;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author mdy
 * @date 2024-06-26 21:37
 * @description
 */
@ConfigurationProperties(prefix = "task.pool")
@Data
public class TaskThreadPoolInfo {
    /**
     *  核心线程数（获取硬件）：线程池创建时候初始化的线程数
     */
    private Integer corePoolSize;
    /**
     * 最大线程数
     */
    private Integer maxPoolSize;
    /**
     * 线程存活时间
     */
    private Integer keepAliveSeconds;
    /**
     * 阻塞队列容量
     */
    private Integer queueCapacity;
}
