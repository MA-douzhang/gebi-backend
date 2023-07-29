package com.madou.user.api.constant;

/**
 * 积分常量
 *
 */
public interface CreditConstant {

    /**
     * 签到积分
     */
    long CREDIT_DAILY = 100;

    /**
     * 生成图表消耗积分
     */
    long CREDIT_CHART_SUCCESS = -1;

    /**
     * 生成图表失败返回积分
     */
    long CREDIT_CHART_FALSE = 1;

    /**
     * 生成文本消耗积分
     */
    long CREDIT_TEXT_SUCCESS = -10;

    /**
     * 生成文本失败返回积分
     */
    long CREDIT_TEXT_FALSE = 10;

    // endregion
}
