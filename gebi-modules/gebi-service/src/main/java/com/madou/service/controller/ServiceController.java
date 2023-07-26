package com.madou.service.controller;

import com.madou.chart.api.service.ChartService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MA_dou
 * @version 1.0
 * @project gebi-cloud
 * @description service服务层
 * @date 2023/7/25 20:55:04
 */
@RestController
@Validated
@RequestMapping("/service")
public class ServiceController {

    @DubboReference
    ChartService chartService;

    @GetMapping("/get")
    public String getString(String str){
        return chartService.getChart(str);
    }
}
