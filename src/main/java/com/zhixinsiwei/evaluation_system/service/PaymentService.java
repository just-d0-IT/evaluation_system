package com.zhixinsiwei.evaluation_system.service;

import com.zhixinsiwei.evaluation_system.common.ApiResponse;

import javax.servlet.http.HttpSession;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName PaymentService.java
 * @Description 支付模块服务类
 * @createTime 2024年12月18日 21:39:00
 */
public interface PaymentService {

    ApiResponse prepay(String openid, String money);

    ApiResponse wxPay(String recordId, HttpSession session);
}
