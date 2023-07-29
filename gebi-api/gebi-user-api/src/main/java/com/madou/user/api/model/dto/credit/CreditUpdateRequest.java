package com.madou.user.api.model.dto.credit;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 */
@Data
public class CreditUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 总积分
     */
    private Long creditTotal;


    private static final long serialVersionUID = 1L;
}
