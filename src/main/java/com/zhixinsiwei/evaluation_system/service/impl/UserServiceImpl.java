package com.zhixinsiwei.evaluation_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationUsers;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.UserEntityService;
import com.zhixinsiwei.evaluation_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName UserServiceImpl.java
 * @Description TODO
 * @createTime 2026年01月29日 20:20:00
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserEntityService userEntityService;

    @Resource
    private WxMpService wxMpService;

    private final String BASE_CALLBACK = "http://jia.szzxsw.cn/api/evaluation_system/user/wxBaseLoginCallback";
    private final String USERINFO_CALLBACK = "http://jia.szzxsw.cn/api/evaluation_system/user/wxUserInfoLoginCallback";

    @Override
    public void wxLogin(HttpServletResponse response, String redirect) throws Exception {
        if (StringUtils.isBlank(redirect)) {
            redirect = "/";
        }
        String url = wxMpService.getOAuth2Service()
                .buildAuthorizationUrl(BASE_CALLBACK, WxConsts.OAuth2Scope.SNSAPI_BASE, redirect);
        log.info("wx login redirect url is {}", url);
        response.sendRedirect(url);
    }

    @Override
    public void wxBaseLoginCallback(String code, String state, HttpSession session, HttpServletResponse response) throws Exception {
        WxOAuth2AccessToken token = wxMpService.getOAuth2Service().getAccessToken(code);
        String openid = token.getOpenId();
        EvaluationUsers user = userEntityService.getOne(new LambdaQueryWrapper<EvaluationUsers>()
                .eq(EvaluationUsers::getUserKey, openid));

        // 数据库已有用户 → 直接登录
        if (user != null) {
            session.setAttribute("uid", user.getId());
            log.info("wx base login call back session id is {},set uid is {}", session.getId(), session.getAttribute("uid"));
            // 设置跨域 cookie
            // String cookieValue = String.format("JSESSIONID=%s; Path=/; Max-Age=3600; HttpOnly; Secure; SameSite=None", session.getId());
            // response.setHeader("Set-Cookie", cookieValue);
            response.sendRedirect(state);
            return;
        }

        // 没有 → 发起授权获取用户信息
        String url = wxMpService.getOAuth2Service()
                .buildAuthorizationUrl(USERINFO_CALLBACK, WxConsts.OAuth2Scope.SNSAPI_USERINFO, "userinfo");
        response.sendRedirect(url);
    }

    @Override
    public void wxUserInfoLoginCallback(String code, String state, HttpSession session, HttpServletResponse response) throws Exception {
        WxOAuth2AccessToken token = wxMpService.getOAuth2Service().getAccessToken(code);
        WxOAuth2UserInfo wxUser = wxMpService.getOAuth2Service().getUserInfo(token, null);

        EvaluationUsers user = new EvaluationUsers();
        user.setUserKey(wxUser.getOpenid());
        user.setUserName(wxUser.getNickname());
        user.setSource("WX_MP");
        user.setExtJson(wxUser.toString());
        userEntityService.save(user);

        session.setAttribute("uid", user.getId());
        log.info("wx user info login call back set uid is {}", session.getAttribute("uid"));
        // 回到最初页面
        response.sendRedirect(state);
    }
}
