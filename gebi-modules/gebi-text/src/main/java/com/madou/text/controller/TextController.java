package com.madou.text.controller;

import com.madou.chart.api.service.ChartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MA_dou
 * @version 1.0
 * @project gebi-cloud
 * @description 文本控制层
 * @date 2023/7/26 16:50:56
 */
@Tag(name = "文本转换服务")
@RestController
@Validated
@RequestMapping("/text")
public class TextController {
    @DubboReference
    ChartService chartService;

    @Operation(summary = "get方法")
    @GetMapping("/get")
    public String getString(String str){
        return chartService.getChart(str);
    }
}
