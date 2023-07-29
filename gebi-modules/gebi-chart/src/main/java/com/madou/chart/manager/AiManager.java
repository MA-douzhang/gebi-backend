package com.madou.chart.manager;

import com.madou.common.common.ErrorCode;
import com.madou.common.excption.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Ai控制器
 */
@Service
public class AiManager {
    @Resource
    private YuCongMingClient client;

    public String doChat(String message,Long modelId){
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);

        // 获取响应
        BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);

        if (response == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 响应错误");
        }
        return response.getData().getContent();
    }

}
