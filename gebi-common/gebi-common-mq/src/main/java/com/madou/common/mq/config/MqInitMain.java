package com.madou.common.mq.config;

import com.madou.common.mq.constant.MqConstant;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于创建队列程序的交换机和队列
 */
@AutoConfiguration
public class MqInitMain {

    /**
     * 支付将死信队列和交换机声明
     */
    @Bean
    Queue AliPayDeadQueue(){
        return QueueBuilder.durable(MqConstant.ORDERS_DEAD_QUEUE_NAME).build();
    }

    @Bean
    DirectExchange AliPayDeadExchange() {
        return new DirectExchange(MqConstant.ORDERS_DEAD_EXCHANGE_NAME);
    }


    @Bean
    Binding AliPayDeadBinding(Queue AliPayDeadQueue, DirectExchange AliPayDeadExchange) {
        return BindingBuilder.bind(AliPayDeadQueue).to(AliPayDeadExchange).with(MqConstant.ORDERS_DEAD_ROUTING_KEY);
    }

    /**
     * 将队列和交换机声明
     */
    @Bean
    Queue AliPayQueue(){
        //信息参数 设置TTL为1min
        Map<String,Object> arg = new HashMap<>();
        arg.put("x-message-ttl",1000*60);
        //绑定死信交换机
        arg.put("x-dead-letter-exchange",MqConstant.ORDERS_DEAD_EXCHANGE_NAME);
        arg.put("x-dead-letter-routing-key",MqConstant.ORDERS_DEAD_ROUTING_KEY);
        return QueueBuilder.durable(MqConstant.ORDERS_QUEUE_NAME).withArguments(arg).build();
    }

    @Bean
    DirectExchange AliPayExchange() {
        return new DirectExchange(MqConstant.ORDERS_EXCHANGE_NAME);
    }

    @Bean
    Binding AliPayBinding(Queue AliPayQueue, DirectExchange AliPayExchange) {
        return BindingBuilder.bind(AliPayQueue).to(AliPayExchange).with(MqConstant.ORDERS_ROUTING_KEY);
    }

    /**
     * 文本转换
     */
    /**
     * 将死信队列和交换机声明
     */
    @Bean
    Queue TxtDeadQueue(){
        return QueueBuilder.durable(MqConstant.TEXT_DEAD_QUEUE_NAME).build();
    }

    @Bean
    DirectExchange TxtDeadExchange() {
        return new DirectExchange(MqConstant.TEXT_DEAD_EXCHANGE_NAME);
    }


    @Bean
    Binding TxtDeadBinding(Queue TxtDeadQueue, DirectExchange TxtDeadExchange) {
        return BindingBuilder.bind(TxtDeadQueue).to(TxtDeadExchange).with(MqConstant.TEXT_DEAD_ROUTING_KEY);
    }

    /**
     * 将队列和交换机声明
     */
    @Bean
    Queue TxtQueue(){
        //信息参数 设置TTL为1min
        Map<String,Object> arg = new HashMap<>();
        arg.put("x-message-ttl",60000);
        //绑定死信交换机
        arg.put("x-dead-letter-exchange",MqConstant.TEXT_DEAD_EXCHANGE_NAME);
        arg.put("x-dead-letter-routing-key",MqConstant.TEXT_DEAD_ROUTING_KEY);
        return QueueBuilder.durable(MqConstant.TEXT_QUEUE_NAME).withArguments(arg).build();
    }

    @Bean
    DirectExchange TxtExchange() {
        return new DirectExchange(MqConstant.TEXT_EXCHANGE_NAME);
    }

    @Bean
    Binding TxtBinding(Queue TxtQueue, DirectExchange TxtExchange) {
        return BindingBuilder.bind(TxtQueue).to(TxtExchange).with(MqConstant.TEXT_ROUTING_KEY);
    }

    /**
     * 图表分析
     */
    /**
     * 将死信队列和交换机声明
     */
    @Bean
    Queue BiDeadQueue(){
        return QueueBuilder.durable(MqConstant.BI_DEAD_QUEUE_NAME).build();
    }

    @Bean
    DirectExchange BiDeadExchange() {
        return new DirectExchange(MqConstant.BI_DEAD_EXCHANGE_NAME);
    }


    @Bean
    Binding BiDeadBinding(Queue BiDeadQueue, DirectExchange BiDeadExchange) {
        return BindingBuilder.bind(BiDeadQueue).to(BiDeadExchange).with(MqConstant.BI_DEAD_ROUTING_KEY);
    }

    /**
     * 将队列和交换机声明
     */
    @Bean
    Queue BiQueue(){
        //信息参数 设置TTL为1min
        Map<String,Object> arg = new HashMap<>();
        arg.put("x-message-ttl",60000);
        //绑定死信交换机
        arg.put("x-dead-letter-exchange",MqConstant.BI_DEAD_EXCHANGE_NAME);
        arg.put("x-dead-letter-routing-key",MqConstant.BI_DEAD_ROUTING_KEY);
        return QueueBuilder.durable(MqConstant.BI_QUEUE_NAME).withArguments(arg).build();
    }

    @Bean
    DirectExchange BiExchange() {
        return new DirectExchange(MqConstant.BI_EXCHANGE_NAME);
    }

    @Bean
    Binding BiBinding(Queue BiQueue, DirectExchange BiExchange) {
        return BindingBuilder.bind(BiQueue).to(BiExchange).with(MqConstant.BI_ROUTING_KEY);
    }
}
