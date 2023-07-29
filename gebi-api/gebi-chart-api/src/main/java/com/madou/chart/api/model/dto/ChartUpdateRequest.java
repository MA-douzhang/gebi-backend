package com.madou.chart.api.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 */
@Data
public class ChartUpdateRequest implements Serializable {


    /**
     * id
     */
    private Long id;
    /**
     * 图表名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chatType;

    /**
     * 生成的图表数据
     */
    private String genChat;

    /**
     * 生成的分析结论
     */
    private String genResult;

    private static final long serialVersionUID = 1L;
}
