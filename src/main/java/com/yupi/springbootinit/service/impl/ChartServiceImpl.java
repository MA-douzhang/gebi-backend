package com.yupi.springbootinit.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.ChartConstant;
import com.yupi.springbootinit.constant.CreditConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.chart.GenChartByAiRequest;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.mapper.ChartMapper;
import com.yupi.springbootinit.service.CreditService;
import com.yupi.springbootinit.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
* @author MA_dou
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2023-05-26 23:18:07
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{


    @Resource
    CreditService creditService;

    @Override
    public String buildUserInput(Chart chart){
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Chart getChartTask(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, User loginUser) {
        String chartType = genChartByAiRequest.getChartType();
        String goal = genChartByAiRequest.getGoal();
        String name = genChartByAiRequest.getName();
        //校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR,"目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name)&&name.length()>=100,ErrorCode.PARAMS_ERROR,"名称不规范");
        //校验文件
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        final long ONE_MB = 1024*1024;
        ThrowUtils.throwIf(size>ONE_MB,ErrorCode.PARAMS_ERROR,"文件超过1MB");
        ThrowUtils.throwIf(size==0,ErrorCode.PARAMS_ERROR,"文件为空");
        //校验文件后缀
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffix = Arrays.asList("png","xlsx","svg","webp","jpeg");
        ThrowUtils.throwIf(!validFileSuffix.contains(suffix),ErrorCode.PARAMS_ERROR,"文件后缀名非法");

        // 压缩后的数据
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        //消耗积分
        Boolean creditResult = creditService.updateCredits(loginUser.getId(), CreditConstant.CREDIT_CHART_SUCCESS);
        ThrowUtils.throwIf(!creditResult,ErrorCode.OPERATION_ERROR,"你的积分不足");
        //保存数据库 wait
        Chart chart = new Chart();
        chart.setUserId(loginUser.getId());
        chart.setChartData(csvData);
        chart.setChatType(chartType);
        chart.setStatus(ChartConstant.WAIT);
        chart.setName(name);
        chart.setGoal(goal);
        boolean saveResult = this.save(chart);
        ThrowUtils.throwIf(!saveResult,ErrorCode.SYSTEM_ERROR,"图表保存失败");
        return chart;
    }

    @Override
    public boolean saveChartAiResult(String result, long chartId) {
        String[] splits = result.split("【【【【【");

        if (splits.length < 3){
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
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI生成图片错误");
        }
        //保存数据库
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus(ChartConstant.SUCCEED);
        updateChartResult.setGenChat(genChart);
        updateChartResult.setGenResult(genResult);
        return this.updateById(updateChartResult);

    }

    @Override
    public void handleChartUpdateError(Long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setStatus(ChartConstant.FAILED);
        updateChartResult.setId(chartId);
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = this.updateById(updateChartResult);
        if (!updateResult){
            log.error("更新图片失败状态失败"+chartId+","+execMessage);
        }
    }
}




