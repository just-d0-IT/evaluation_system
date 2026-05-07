package com.zhixinsiwei.evaluation_system.service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName UserService.java
 * @Description TODO
 * @createTime 2026年01月29日 20:20:00
 */
public interface UserService {

    /**
     * 微信登录
     *
     * @param response
     */
    void wxLogin(HttpServletResponse response, String redirect) throws Exception;

    /**
     * 微信静默登录回调
     *
     * @param code
     * @param response
     */
    void wxBaseLoginCallback(String code, String state,HttpSession session, HttpServletResponse response) throws Exception;

    /**
     * 微信授权登录回调
     *
     * @param code
     * @param session
     * @param response
     */
    void wxUserInfoLoginCallback(String code, String state,HttpSession session, HttpServletResponse response) throws Exception;
}
