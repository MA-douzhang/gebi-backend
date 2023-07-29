package com.madou.chart.service.impl;

import com.madou.chart.api.service.Chart1Service;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author MA_dou
 * @version 1.0
 * @project gebi-cloud
 * @description
 * @date 2023/7/25 21:37:59
 */

@DubboService
@Service
public class Chart1ServiceImpl implements Chart1Service {
    @Override
    public String getChart(String chart) {
        return "im chart push"+chart;
    }
}
