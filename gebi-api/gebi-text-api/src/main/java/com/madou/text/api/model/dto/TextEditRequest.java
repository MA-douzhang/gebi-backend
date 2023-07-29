package com.madou.text.api.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑请求
 *
 */
@Data
public class TextEditRequest implements Serializable {
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
     * 创建用户Id
     */
    private Long userId;

    /**
     * wait,running,succeed,failed
     */
    private String status;


    private static final long serialVersionUID = 1L;
}
