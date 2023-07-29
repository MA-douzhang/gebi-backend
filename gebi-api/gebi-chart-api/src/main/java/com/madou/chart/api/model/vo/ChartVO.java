package com.madou.chart.api.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 图表信息表(脱敏)
 */
@Data
public class ChartVO implements Serializable {

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
    /**
     * 执行状态
     */
    private String status;
    /**
     * 执行信息
     */
    private String execMessage;

    /**
     * 创建时间
     */
    private Date createTime;


    private static final long serialVersionUID = 1L;
}
