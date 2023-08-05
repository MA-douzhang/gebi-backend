package com.madou.user.api.constant;

/**
 * 支付订单常量
 *
 */
public interface OrdersConstant {

    /**
     * 回调地址(本地部署时需要内网穿透)
     */
    //todo 可以在配置文件中修改
    String NOTIFYURL = "http://60.204.157.128:8099/userApi/alipay/notify";

    /**
     * 未支付
     */
    String UNPAID = "unpaid";

    //  region 权限

    /**
     * 支付中
     */
    String PAYING = "paying";

    /**
     * 成功
     */
    String SUCCEED = "succeed";

    /**
     * 失败
     */
    String FAILED = "failed";


}
