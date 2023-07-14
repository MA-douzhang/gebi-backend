package com.yupi.springbootinit.model.vo;

import lombok.Data;

@Data
public class AiChartResponse {
    /**
     * 图表数据
     */
    private String genChart;
    /**
     * 图表结论
     */
    private String genResult;
    /**
     * 图表id
     */
    private Long chartId;
}
