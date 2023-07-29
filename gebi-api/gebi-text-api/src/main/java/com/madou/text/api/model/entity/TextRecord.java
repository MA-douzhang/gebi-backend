package com.madou.text.api.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文本记录表
 * @TableName text_record
 */
@TableName(value ="text_record")
@Data
public class TextRecord implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文本任务id
     */
    private Long textTaskId;

    /**
     * 文本内容
     */
    private String textContent;

    /**
     * 生成的文本内容
     */
    private String genTextContent;

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
