package com.madou.common.mq.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 公共队列生产者
 */

@AutoConfiguration
public class MqMessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发信息
     * @param exchange
     * @param routingKey
     * @param message
     */
    public void sendMessage(String exchange,String routingKey,String message){
        rabbitTemplate.convertAndSend(exchange,routingKey,message);
    }
}
