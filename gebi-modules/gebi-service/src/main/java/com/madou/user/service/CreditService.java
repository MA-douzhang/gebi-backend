package com.madou.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.madou.user.api.model.entity.Credit;


/**
* @author MA_dou
* @description 针对表【credit(积分表)】的数据库操作Service
* @createDate 2023-06-28 21:29:40
*/
public interface CreditService extends IService<Credit> {
    /**
     * 根据 当前用户ID 获取积分总数
     * @param userId
     * @return
     */
    Long getCreditTotal(Long userId);

    /**
     * 每日签到
     * @param userId
     * @return
     */
    Boolean signUser(Long userId);

    /**
     * 更新积分（内部方法） 正数为增加积分，负数为消耗积分
     * @param userId
     * @param credits
     * @return
     */
    Boolean updateCredits(Long userId,long credits);

}
