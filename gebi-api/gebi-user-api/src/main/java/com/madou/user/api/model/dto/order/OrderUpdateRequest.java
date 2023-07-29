package com.madou.user.api.model.dto.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 */
@Data
public class OrderUpdateRequest implements Serializable {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 支付宝交易凭证id
     */
    private String alipayTradeNo;

    /**
     * unpaid,paying,succeed,failed
     */
    private String tradeStatus;

    /**
     * 支付宝买家id
     */
    private String buyerId;


    private static final long serialVersionUID = 1L;
}
