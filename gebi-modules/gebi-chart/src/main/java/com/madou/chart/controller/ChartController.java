package com.madou.chart.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.madou.chart.api.constant.ChartConstant;
import com.madou.chart.api.model.dto.*;
import com.madou.chart.api.model.entity.Chart;
import com.madou.chart.api.model.vo.ChartVO;
import com.madou.chart.service.ChartService;
import com.madou.common.ai.config.AiManager;
import com.madou.common.annotation.AuthCheck;
import com.madou.common.common.BaseResponse;
import com.madou.common.common.DeleteRequest;
import com.madou.common.common.ErrorCode;
import com.madou.common.common.ResultUtils;
import com.madou.common.constant.CommonConstant;
import com.madou.common.constant.MqConstant;
import com.madou.common.excption.BusinessException;
import com.madou.common.excption.ThrowUtils;
import com.madou.common.model.vo.AiResponse;
import com.madou.common.mq.config.MqMessageProducer;
import com.madou.common.utils.SqlUtils;
import com.madou.user.api.InnerUserService;
import com.madou.user.api.constant.UserConstant;
import com.madou.user.api.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图表分析接口
 *
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService chartService;

    @DubboReference
    private InnerUserService userService;

    @Resource
    private AiManager aiManager;


    @Resource
    private MqMessageProducer mqMessageProducer;

    // region 增删改查

    /**
     * 创建
     *
     * @param chartAddRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);

        User loginUser = userService.getLoginUser();
        chart.setUserId(loginUser.getId());
        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = chart.getId();
        return ResultUtils.success(newChartId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser();
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChart.getUserId().equals(user.getId()) && !userService.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = chartService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param chartUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);
        long id = chartUpdateRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Chart> getChartById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(chart);
    }
    /**
     * 根据 id 获取 图表脱敏
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<ChartVO> getChartVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        ChartVO chartVO = new ChartVO();
        BeanUtils.copyProperties(chart,chartVO);
        return ResultUtils.success(chartVO);
    }
    /**
     * 分页获取列表（封装类）
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
            HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param chartQueryRequest
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryRequest chartQueryRequest) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser();
        chartQueryRequest.setUserId(loginUser.getId());
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    // endregion


    /**
     * 编辑（图表）
     *
     * @param chartEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest, HttpServletRequest request) {
        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartEditRequest, chart);
        User loginUser = userService.getLoginUser();
        long id = chartEditRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }
    private QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();

        if (chartQueryRequest == null) {
            return queryWrapper;
        }

        String chatType = chartQueryRequest.getChatType();
        String goal = chartQueryRequest.getGoal();
        String name = chartQueryRequest.getName();
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();
        Long id = chartQueryRequest.getId();
        Long userId = chartQueryRequest.getUserId();


        queryWrapper.eq(id!=null &&id>0,"id",id);
        queryWrapper.like(StringUtils.isNotEmpty(name),"name",name);
        queryWrapper.eq(StringUtils.isNoneBlank(goal),"goal",goal);
        queryWrapper.eq(StringUtils.isNoneBlank(chatType),"chartType",chatType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId),"userId",userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
    /**
     * 图表数据上传(同步)
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @return
     */
    @PostMapping("/gen")
    public BaseResponse<AiResponse> genChartAi(@RequestPart("file") MultipartFile multipartFile,
                                               GenChartByAiRequest genChartByAiRequest) {

        User loginUser = userService.getLoginUser();
        //获取任务表数据
        Chart chartTask = chartService.getChartTask(multipartFile, genChartByAiRequest, loginUser);

        String result = aiManager.doChat(chartService.buildUserInput(chartTask), ChartConstant.MODE_ID);
        //处理返回的数据
        boolean saveResult = chartService.saveChartAiResult(result, chartTask.getId());
        if (!saveResult){
            chartService.handleChartUpdateError(chartTask.getId(), "图表数据保存失败");
        }
        //返回数据参数
        AiResponse aiResponse = new AiResponse();
        aiResponse.setResultId(chartTask.getId());
        return ResultUtils.success(aiResponse);

    }

//    /**
//     * 图表数据上传(异步)
//     *
//     * @param multipartFile
//     * @param genChartByAiRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/gen/async")
//    public BaseResponse<AiResponse> genChartAsyncAi(@RequestPart("file") MultipartFile multipartFile,
//                                                    GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
//
//        User loginUser = userService.getLoginUser(request);
//        //获取任务表数据
//        Chart chartTask = chartService.getChartTask(multipartFile, genChartByAiRequest, loginUser);
//
//        //todo 需要处理队列满后的异常
//        try {
//            CompletableFuture.runAsync(()->{
//                //更改图片状态为 running
//                Chart updateChart = new Chart();
//                updateChart.setId(chartTask.getId());
//                updateChart.setStatus(ChartConstant.RUNNING);
//                boolean updateResult = chartService.updateById(updateChart);
//                if (!updateResult){
//                    chartService.handleChartUpdateError(chartTask.getId(),"更新图表执行状态失败");
//                    return;
//                }
//                //调用AI
//                String result = aiManager.doChat(chartService.buildUserInput(chartTask),ChartConstant.MODE_ID);
//                //处理返回的数据
//                boolean saveResult = chartService.saveChartAiResult(result, chartTask.getId());
//                if (!saveResult){
//                    chartService.handleChartUpdateError(chartTask.getId(), "图表数据保存失败");
//                }
//            },threadPoolExecutor);
//        } catch (Exception e) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"系统繁忙，请稍后重试");
//        }
//        //返回数据参数
//        AiResponse aiResponse = new AiResponse();
//        aiResponse.setResultId(chartTask.getId());
//        return ResultUtils.success(aiResponse);
//
//    }
//
    /**
     * 图表数据上传(mq)
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @return
     */
    @PostMapping("/gen/async/mq")
    public BaseResponse<AiResponse> genChartAsyncAiMq(@RequestPart("file") MultipartFile multipartFile,
                                                      GenChartByAiRequest genChartByAiRequest) {
        User loginUser = userService.getLoginUser();

        //获取任务表数据
        Chart chartTask = chartService.getChartTask(multipartFile, genChartByAiRequest, loginUser);

        Long chartId = chartTask.getId();
        log.warn("准备发送信息给队列，Message={}=======================================",chartId);
        mqMessageProducer.sendMessage(MqConstant.BI_EXCHANGE_NAME,MqConstant.BI_ROUTING_KEY,String.valueOf(chartId));
        //返回数据参数
        AiResponse aiResponse = new AiResponse();
        aiResponse.setResultId(chartTask.getId());
        return ResultUtils.success(aiResponse);

    }


    /**
     * 图表重新生成(mq)
     *
     * @param chartRebuildRequest
     * @return
     */
    @PostMapping("/gen/async/rebuild")
    public BaseResponse<AiResponse> genChartAsyncAiRebuild(ChartRebuildRequest chartRebuildRequest) {
        Long chartId = chartRebuildRequest.getId();
        Chart genChartByAiRequest = chartService.getById(chartId);
        String chartType = genChartByAiRequest.getChatType();
        String goal = genChartByAiRequest.getGoal();
        String name = genChartByAiRequest.getName();
        String chartData = genChartByAiRequest.getChartData();

        //校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal),ErrorCode.PARAMS_ERROR,"目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name)&&name.length()>=100,ErrorCode.PARAMS_ERROR,"名称过长");
        ThrowUtils.throwIf(StringUtils.isBlank(chartData),ErrorCode.PARAMS_ERROR,"表格数据为空");
        ThrowUtils.throwIf(StringUtils.isBlank(chartType),ErrorCode.PARAMS_ERROR,"生成表格类型为空");

        User loginUser = userService.getLoginUser();

        //保存数据库 wait
        Chart chart = new Chart();
        chart.setStatus(ChartConstant.WAIT);
        chart.setId(chartId);
        boolean saveResult = chartService.updateById(chart);
        ThrowUtils.throwIf(!saveResult,ErrorCode.SYSTEM_ERROR,"图表保存失败");
        log.warn("准备发送信息给队列，Message={}=======================================",chartId);
        mqMessageProducer.sendMessage(MqConstant.BI_EXCHANGE_NAME,MqConstant.BI_ROUTING_KEY,String.valueOf(chartId));
        //返回数据参数
        AiResponse aiResponse = new AiResponse();
        aiResponse.setResultId(chart.getId());
        return ResultUtils.success(aiResponse);

    }


}
