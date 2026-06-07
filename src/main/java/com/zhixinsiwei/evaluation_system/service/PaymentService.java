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

    /**
     * 处理微信支付异步回调
     * @param body 请求体
     * @param serialNumber 证书序列号
     * @param nonce 随机数
     * @param signature 签名
     * @param timestamp 时间戳
     * @return 0-成功，1-签名验证失败，2-业务处理失败
     */
    int handleNotify(String body, String serialNumber, String nonce, String signature, String timestamp);
}
