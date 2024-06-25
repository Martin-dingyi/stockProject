package com.mdy.stock.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mdy
 * @date 2024-06-26 0:18
 * @description
 */
@Configuration
public class RabbitMqConfig {
    /**
     * 重新定义消息序列化的方式，改为基于json格式序列化和反序列化
     * @return MessageConverter
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 国内大盘信息队列
     * @return 名为innerMarketQueue的Queue
     */
    @Bean
    public Queue innerMarketQueue(){
        return new Queue("innerMarketQueue", true);
    }

    /**
     * 国内股票信息队列
     * @return 名为innerStockQueue的Queue
     */
    @Bean
    public Queue innerStockQueue(){
        return new Queue("innerStockQueue", true);
    }

    /**
     * 定义路由股票信息的交换机
     * @return 名为stockExchange交换机
     */
    @Bean
    public TopicExchange innerMarketTopicExchange(){
        return new TopicExchange("stockExchange",true,false);
    }

    /**
     * 绑定innerMarketQueue队列到指定交换机
     * @return 路由信息为inner.market的Binding
     */
    @Bean
    public Binding marketQueueBindingExchange(){
        return BindingBuilder.bind(innerMarketQueue()).to(innerMarketTopicExchange())
                .with("inner.market");
    }

    /**
     * 绑定innerStockQueue队列到指定交换机
     * @return 路由信息为inner.market的Binding
     */
    @Bean
    public Binding stockQueueBindingExchange(){
        return BindingBuilder.bind(innerStockQueue()).to(innerMarketTopicExchange())
                .with("inner.stock");
    }

}
