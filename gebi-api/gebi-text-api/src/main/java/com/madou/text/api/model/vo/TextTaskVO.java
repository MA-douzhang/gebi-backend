package com.madou.text.api.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class TextTaskVO {

    /**
     * 任务id
     */
    private Long id;

    /**
     * 笔记名称
     */
    private String name;

    /**
     * 文本类型
     */
    private String textType;

    /**
     * 生成的文本内容
     */
    private String genTextContent;

    /**
     * wait,running,succeed,failed
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;


    private static final long serialVersionUID = 1L;
}
