package com.yupi.springbootinit.model.dto.credit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 编辑请求
 *
 */
@Data
public class CreditEditRequest implements Serializable {

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
