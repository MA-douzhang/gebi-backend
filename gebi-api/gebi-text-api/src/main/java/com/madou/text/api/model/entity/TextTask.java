package com.madou.text.api.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文本任务表
 * @TableName text_task
 */
@TableName(value ="text_task")
@Data
public class TextTask implements Serializable {
    /**
     * 任务id
     */
    @TableId(type = IdType.ASSIGN_ID)
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

    /**
     * 执行信息
     */
    private String execMessage;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
