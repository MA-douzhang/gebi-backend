package com.yupi.springbootinit.mq.Bi;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.ChartConstant;
import com.yupi.springbootinit.constant.MqConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.manager.AiManager;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 图表分析消费者队列
 */
@Component
@Slf4j
public class BiMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    @SneakyThrows
    @RabbitListener(queues = {MqConstant.BI_QUEUE_NAME},ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        log.warn("接收到队列信息，receiveMessage={}=======================================",message);
        if (StringUtils.isBlank(message)){
            //消息为空，消息拒绝，不重复发送，不重新放入队列
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null){
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"图表为空");
        }

        //修改表状态为执行中，执行成功修改为“已完成”；执行失败修改为“失败”
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus(ChartConstant.RUNNING);
        boolean updateResult = chartService.updateById(updateChart);
        if (!updateResult){
            handleChartUpdateError(chart.getId(),"更新图表执行状态失败");
            return;
        }
        //调用AI
        String result = null;
        try {
            result = aiManager.doChat(buildUserInput(chart).toString(), ChartConstant.MODE_ID);
        } catch (Exception e) {
            channel.basicNack(deliveryTag,false,true);
            log.warn("信息放入队列{}", DateTime.now());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 服务错误");
        }
        String[] splits = result.split("【【【【【");
        if (splits.length < 3){
            log.warn("AI生成错误，重新放入队列");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 生成错误");
        }
        //todo 可以使用正则表达式保证数据准确性，防止中文出现
        String genChart= splits[1].trim();
        String genResult = splits[2].trim();
        //将非js格式转化为js格式
        try {
            HashMap<String,Object> genChartJson = JSONUtil.toBean(genChart, HashMap.class);
            genChart = JSONUtil.toJsonStr(genChartJson);
        } catch (Exception e) {
            log.warn("AI生成错误，重新放入队列");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI生成图片错误");
        }
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setStatus(ChartConstant.SUCCEED);
        updateChartResult.setGenChat(genChart);
        updateChartResult.setGenResult(genResult);
        updateResult = chartService.updateById(updateChartResult);
        if (!updateResult){
            handleChartUpdateError(chart.getId(), "更新图片成功状态失败");
        }
        //消息确认
        channel.basicAck(deliveryTag,false);
    }
    private String buildUserInput(Chart chart){
        String goal = chart.getGoal();
        String chatType = chart.getChatType();
        String csvData = chart.getChartData();
        //构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chatType)) {
            userGoal += "，请使用" + chatType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }
    private void handleChartUpdateError(Long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setStatus(ChartConstant.FAILED);
        updateChartResult.setId(chartId);
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult){
            log.error("更新图片失败状态失败"+chartId+","+execMessage);
        }
    }
}
