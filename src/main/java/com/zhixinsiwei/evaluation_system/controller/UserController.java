package com.zhixinsiwei.evaluation_system.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zhixinsiwei.evaluation_system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName UserController.java
 * @Description TODO
 * @createTime 2026年01月29日 20:08:00
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "用户登录判断")
    @ApiOperationSupport(author = "ovo")
    @GetMapping("/me")
    public void me(HttpSession session, HttpServletResponse response) throws Exception {
        String uid = (String) session.getAttribute("uid");
        log.info("get uid result is {},session id is {}", uid, session.getId());
        if (uid == null) {
            // 未登录
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        // 已登录
        response.setStatus(HttpStatus.OK.value());
    }

    @ApiOperation(value = "微信登录")
    @ApiOperationSupport(author = "ovo")
    @GetMapping("/wxLogin")
    public void wxLogin(HttpServletResponse response, String redirect) throws Exception {
        userService.wxLogin(response, redirect);
    }

    @ApiOperation(value = "微信静默登录回调")
    @ApiOperationSupport(author = "ovo")
    @GetMapping("/wxBaseLoginCallback")
    public void wxBaseLoginCallback(String code, String state, HttpSession session, HttpServletResponse response) throws Exception {
        userService.wxBaseLoginCallback(code, state, session, response);
    }

    @ApiOperation(value = "微信授权登录回调")
    @ApiOperationSupport(author = "ovo")
    @GetMapping("/wxUserInfoLoginCallback")
    public void wxUserInfoLoginCallback(String code, String state, HttpSession session, HttpServletResponse response) throws Exception {
        userService.wxUserInfoLoginCallback(code, state, session, response);
    }
}
