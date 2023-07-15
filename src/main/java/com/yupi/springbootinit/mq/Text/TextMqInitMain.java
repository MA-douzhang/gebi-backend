package com.yupi.springbootinit.mq.Text;

import com.yupi.springbootinit.constant.MqConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于创建程序的交换机和队列
 */
@Configuration
public class TextMqInitMain {

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


}
