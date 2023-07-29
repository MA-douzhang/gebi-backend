package com.madou.user.api.model.dto.order;

import lombok.Data;

import java.io.Serializable;


/**
 * 创建充值订单表
 * @TableName orders
 */
@Data
public class OrderAddRequest implements Serializable {

    /**
     * 交易名称
     */
    private String subject;

    /**
     * 交易金额
     */
    private Double totalAmount;

    private static final long serialVersionUID = 1L;
}
