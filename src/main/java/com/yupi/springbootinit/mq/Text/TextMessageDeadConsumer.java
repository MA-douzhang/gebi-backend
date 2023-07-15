package com.yupi.springbootinit.mq.Text;

import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.MqConstant;
import com.yupi.springbootinit.constant.TextConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.entity.TextTask;
import com.yupi.springbootinit.service.TextTaskService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 文本转换队列的死信队列
 */
@Component
@Slf4j
public class TextMessageDeadConsumer {

    @Resource
    private TextTaskService textTaskService;


    @SneakyThrows
    @RabbitListener(queues = {MqConstant.TEXT_DEAD_QUEUE_NAME},ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        log.warn("接收到死信队列信息，receiveMessage={}=======================================",message);
        if (StringUtils.isBlank(message)){
            //消息为空，消息拒绝，不重复发送，不重新放入队列
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"消息为空");
        }
        long textTaskId = Long.parseLong(message);
        TextTask textTask = textTaskService.getById(textTaskId);
        if (textTask == null){
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"文本为空");
        }

        //修改表状态为执行中，执行成功修改为“已完成”；执行失败修改为“失败”
        TextTask updateTextTask = new TextTask();
        updateTextTask.setId(textTask.getId());
        updateTextTask.setStatus(TextConstant.FAILED);
        boolean updateResult = textTaskService.updateById(updateTextTask);
        //这里不对记录表状态修改，记录只能内部使用
        if (!updateResult){
            handleTextTaskUpdateError(updateTextTask.getId(),"更新图表执行状态失败");
            return;
        }
        //消息确认
        channel.basicAck(deliveryTag,false);
    }
    private void handleTextTaskUpdateError(Long chartId, String execMessage) {
        TextTask updateTextTaskResult = new TextTask();
        updateTextTaskResult.setStatus(TextConstant.FAILED);
        updateTextTaskResult.setId(chartId);
        updateTextTaskResult.setExecMessage(execMessage);
        boolean updateResult = textTaskService.updateById(updateTextTaskResult);
        if (!updateResult){
            log.error("更新图片失败状态失败"+chartId+","+execMessage);
        }
    }
}
