package com.madou.chart.controller;

import com.madou.chart.api.service.Chart1Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author MA_dou
 * @version 1.0
 * @project gebi-cloud
 * @description service服务层
 * @date 2023/7/25 20:55:04
 */
@RestController
@Validated
@RequestMapping("/chart1")
public class Chart1Controller {

    @Resource
    Chart1Service chart1Service;

    @GetMapping("/get")
    public String getString(String str){
        return chart1Service.getChart(str);
    }
}
