package com.yupi.springbootinit.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.CreditConstant;
import com.yupi.springbootinit.constant.TextConstant;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.text.GenTextTaskByAiRequest;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.model.entity.TextRecord;
import com.yupi.springbootinit.model.entity.TextTask;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.service.CreditService;
import com.yupi.springbootinit.service.TextRecordService;
import com.yupi.springbootinit.service.TextTaskService;
import com.yupi.springbootinit.mapper.TextTaskMapper;
import com.yupi.springbootinit.utils.TxtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* @author MA_dou
* @description 针对表【text_task(文本任务表)】的数据库操作Service实现
* @createDate 2023-07-12 20:32:15
*/
@Service
public class TextTaskServiceImpl extends ServiceImpl<TextTaskMapper, TextTask>
    implements TextTaskService{

    @Resource
    private CreditService creditService;

    @Resource
    private TextRecordService textRecordService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TextTask getTextTask(MultipartFile multipartFile, GenTextTaskByAiRequest genTextTaskByAiRequest, User loginUser) {
        String textTaskType = genTextTaskByAiRequest.getTextType();
        String name = genTextTaskByAiRequest.getName();
        //校验
        ThrowUtils.throwIf(StringUtils.isBlank(textTaskType), ErrorCode.PARAMS_ERROR,"目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name)&&name.length()>=100,ErrorCode.PARAMS_ERROR,"名称过长");
        //校验文件
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        final long ONE_MB = 1024*1024;
        ThrowUtils.throwIf(size>ONE_MB,ErrorCode.PARAMS_ERROR,"文件超过1MB");
        ThrowUtils.throwIf(size==0,ErrorCode.PARAMS_ERROR,"文件为空");
        //校验文件后缀
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffix = Arrays.asList("txt");
        ThrowUtils.throwIf(!validFileSuffix.contains(suffix),ErrorCode.PARAMS_ERROR,"文件后缀名非法");

        //消耗积分
        Boolean creditResult = creditService.updateCredits(loginUser.getId(), CreditConstant.CREDIT_CHART_SUCCESS);
        ThrowUtils.throwIf(!creditResult,ErrorCode.OPERATION_ERROR,"你的积分不足");

        //保存数据库 wait
        //保存任务进数据库
        TextTask textTask = new TextTask();
        textTask.setTextType(textTaskType);
        textTask.setName(name);
        textTask.setUserId(loginUser.getId());
        textTask.setStatus(TextConstant.WAIT);
        boolean saveResult = this.save(textTask);
        ThrowUtils.throwIf(!saveResult,ErrorCode.SYSTEM_ERROR,"文本任务保存失败");

        Long taskId = textTask.getId();
        // 压缩后的数据
        ArrayList<String> textContentList = TxtUtils.readerFile(multipartFile);
        ThrowUtils.throwIf(textContentList.size() ==0,ErrorCode.PARAMS_ERROR,"文件为空");

        //将分割的内容保存入记录表
        ArrayList<TextRecord> taskArrayList = new ArrayList<>();
        textContentList.forEach(textContent ->{
            TextRecord textRecord = new TextRecord();
            textRecord.setTextTaskId(taskId);
            textRecord.setTextContent(textContent);
            textRecord.setStatus(TextConstant.WAIT);
            taskArrayList.add(textRecord);
        });

        boolean batchResult = textRecordService.saveBatch(taskArrayList);
        ThrowUtils.throwIf(!batchResult,ErrorCode.SYSTEM_ERROR,"文本记录保存失败");

        return textTask;
    }

    @Override
    public void handleTextTaskUpdateError(Long textTaskId, String execMessage) {
        TextTask updateTextTaskResult = new TextTask();
        updateTextTaskResult.setStatus(TextConstant.FAILED);
        updateTextTaskResult.setId(textTaskId);
        updateTextTaskResult.setExecMessage(execMessage);
        boolean updateResult = this.updateById(updateTextTaskResult);
        if (!updateResult){
            log.error("更新文本失败状态失败"+textTaskId+","+execMessage);
        }
    }


}




