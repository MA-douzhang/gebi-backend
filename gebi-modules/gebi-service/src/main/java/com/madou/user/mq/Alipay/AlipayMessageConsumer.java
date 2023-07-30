package com.madou.user.mq.Alipay;

import com.madou.common.common.ErrorCode;
import com.madou.common.constant.MqConstant;
import com.madou.common.excption.BusinessException;
import com.madou.user.api.constant.OrdersConstant;
import com.madou.user.api.model.entity.Orders;
import com.madou.user.service.OrdersService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 阿里支付队列消费者
 */
@Component
@Slf4j
public class AlipayMessageConsumer {


    @Resource
    OrdersService ordersService;

    @SneakyThrows
    @RabbitListener(queues = {MqConstant.ORDERS_QUEUE_NAME})
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        log.warn("接收到队列信息，receiveMessage={}=======================================",message);
        if (StringUtils.isBlank(message)){
            //消息为空，消息拒绝，不重复发送，不重新放入队列
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"消息为空");
        }
        long orderId = Long.parseLong(message);
        Orders order = ordersService.getById(orderId);
        if (order == null){
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"订单为空");
        }
        //查询订单信息看是完成支付，未支付则重新放入队列中直至过期
        String tradeStatus = order.getTradeStatus();
        log.warn("订单查询为"+order.getTradeStatus());
        if (!tradeStatus.equals(OrdersConstant.SUCCEED)){
            log.warn("订单未支付成功,重新放回队列,订单号为"+order.getId());
            channel.basicNack(deliveryTag,false,true);
        }else {
            //消息确认
            channel.basicAck(deliveryTag,false);
        }
    }
}
