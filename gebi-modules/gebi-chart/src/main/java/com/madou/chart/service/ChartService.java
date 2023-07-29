package com.madou.chart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.madou.chart.api.model.dto.GenChartByAiRequest;
import com.madou.chart.api.model.entity.Chart;
import com.madou.user.api.model.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
* @author MA_dou
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2023-05-26 23:18:07
*/
public interface ChartService extends IService<Chart> {

    /**
     * 图表用户输入构造
     * @param chart
     * @return
     */
    String buildUserInput(Chart chart);
    /**
     * 处理Ai返回信息保存
     * @param result
     * @return
     */
    boolean saveChartAiResult(String result, long chartId);

    /**
     * 图表更新失败
     * @param chartId
     * @param execMessage
     */
    void handleChartUpdateError(Long chartId, String execMessage);
    /**
     * 获取准备分析的表数据(事务回滚)
     * @param multipartFile
     * @param genChartByAiRequest
     * @param loginUser
     * @return
     */
    Chart getChartTask(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, User loginUser);

}
