package com.madou.user.api.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 充值订单表
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders implements Serializable {
    /**
     * 订单id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 支付宝交易凭证id
     */
    private String alipayTradeNo;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 交易名称
     */
    private String subject;

    /**
     * 交易金额
     */
    private Double totalAmount;

    /**
     * unpaid,paying,succeed,failed
     */
    private String tradeStatus;

    /**
     * 支付宝买家id
     */
    private String buyerId;

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
