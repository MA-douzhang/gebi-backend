package com.yupi.springbootinit.model.dto.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
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
