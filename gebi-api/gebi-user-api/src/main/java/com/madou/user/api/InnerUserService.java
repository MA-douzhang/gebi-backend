package com.madou.user.api;

import com.madou.user.api.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 远程用户服务 user
 *
 */
public interface InnerUserService {

    /**
     * 获取当前登录用户
     *
     * @return
     */
    User getLoginUser();


    /**
     * 是否为管理员
     *
     * @return
     */
    boolean isAdmin();

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);



}
