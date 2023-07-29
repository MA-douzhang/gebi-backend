package com.madou.common.constant;

/**
 * mq初始化常量
 */
public interface MqConstant {
    String BI_EXCHANGE_NAME = "bi_exchange";

    String BI_QUEUE_NAME="bi_queue";

    String BI_ROUTING_KEY="bi_routingKey";
    //死信
    String BI_DEAD_EXCHANGE_NAME="bi_dead_exchange";
    String BI_DEAD_QUEUE_NAME="bi_dead_queue";

    String BI_DEAD_ROUTING_KEY="bi_dead_routingKey";


    //订单支付队列
    String ORDERS_EXCHANGE_NAME = "orders_exchange";

    String ORDERS_QUEUE_NAME="orders_queue";

    String ORDERS_ROUTING_KEY="orders_routingKey";
    //死信
    String ORDERS_DEAD_EXCHANGE_NAME="orders_dead_exchange";
    String ORDERS_DEAD_QUEUE_NAME="orders_dead_queue";

    String ORDERS_DEAD_ROUTING_KEY="orders_dead_routingKey";

    //文本AI生成队列

    String TEXT_EXCHANGE_NAME = "text_exchange";

    String TEXT_QUEUE_NAME="text_queue";

    String TEXT_ROUTING_KEY="text_routingKey";
    //死信
    String TEXT_DEAD_EXCHANGE_NAME="text_dead_exchange";
    String TEXT_DEAD_QUEUE_NAME="text_dead_queue";

    String TEXT_DEAD_ROUTING_KEY="text_dead_routingKey";
}
