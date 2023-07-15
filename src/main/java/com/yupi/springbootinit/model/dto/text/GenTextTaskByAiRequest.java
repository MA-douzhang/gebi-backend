package com.yupi.springbootinit.model.dto.text;

import lombok.Data;

import java.io.Serializable;

/**
 * 文本生成请求
 *
 */
@Data
public class GenTextTaskByAiRequest implements Serializable {

    /**
     * 笔记名称
     */
    private String name;

    /**
     * 文本类型
     */
    private String textType;



    private static final long serialVersionUID = 1L;
}
