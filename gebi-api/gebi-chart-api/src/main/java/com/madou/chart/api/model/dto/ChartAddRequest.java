package com.madou.chart.api.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class ChartAddRequest implements Serializable {

    /**
     * 分析目标
     */
    private String goal;
    /**
     * 图表名称
     */
    private String name;

    /**
     * 图表数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chatType;



    private static final long serialVersionUID = 1L;
}
