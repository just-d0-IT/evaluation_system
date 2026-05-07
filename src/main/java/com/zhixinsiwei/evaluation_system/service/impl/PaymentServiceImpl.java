package com.zhixinsiwei.evaluation_system.service.impl;

import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.zhixinsiwei.evaluation_system.common.ApiResponse;
import com.zhixinsiwei.evaluation_system.common.constant.WXPayConstants;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationRecords;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationUsers;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.RecordService;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.UserEntityService;
import com.zhixinsiwei.evaluation_system.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.SecureRandom;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName PaymentServiceImpl.java
 * @Description 支付模块服务实现类
 * @createTime 2024年12月18日 21:40:00
 */
@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private JsapiServiceExtension jsService;

    @Resource
    private RecordService recordService;

    @Resource
    private UserEntityService userEntityService;

    @Override
    public ApiResponse prepay(String openid, String money) {
        log.info("openid is {},money is {}", openid, money);
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        BigDecimal moneyNumber = new BigDecimal(money);
        // 将金额转换为分
        int total = moneyNumber.multiply(new BigDecimal("100")).intValue();
        amount.setTotal(total);
        amount.setCurrency("CNY");
        request.setAmount(amount);
        Payer payer = new Payer();
        //需要当前支付用户的openid
        payer.setOpenid(openid);
        request.setAmount(amount);
        request.setPayer(payer);
        request.setAppid(WXPayConstants.APPID); //小程序id
        request.setMchid(WXPayConstants.MERCHANT_ID);   //商户id
        request.setDescription("智商测试国际版公众号测试支付"); //详情
        request.setNotifyUrl(WXPayConstants.NOTIFY_URL);    //回调
        //String orderNumber = generateOrderNumber(30);
        String orderNumber = generateNonceStr();
        log.info("生成的订单号：{}", orderNumber);
        request.setOutTradeNo(orderNumber);
        PrepayWithRequestPaymentResponse response = jsService.prepayWithRequestPayment(request);
        log.info("JSApi支付参数：{}，响应：{}", request, response);

        return ApiResponse.success(response);
    }


    @Override
    public ApiResponse wxPay(String recordId, HttpSession session) {
        String userId = (String) session.getAttribute("uid");
        EvaluationUsers user = userEntityService.getById(userId);
        EvaluationRecords record = recordService.getById(recordId);
        log.info("user is {},record is {}", user.toString(), record.toString());
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        BigDecimal moneyNumber = new BigDecimal(record.getPayPrice());
        // 将金额转换为分
        int total = moneyNumber.multiply(new BigDecimal("100")).intValue();
        amount.setTotal(total);
        amount.setCurrency("CNY");
        request.setAmount(amount);
        Payer payer = new Payer();
        //需要当前支付用户的openid
        payer.setOpenid(user.getUserKey());
        request.setAmount(amount);
        request.setPayer(payer);
        request.setAppid(WXPayConstants.APPID); //小程序id
        request.setMchid(WXPayConstants.MERCHANT_ID);   //商户id
        request.setDescription("智商测试国际版公众号测试支付"); //详情
        request.setNotifyUrl(WXPayConstants.NOTIFY_URL);    //回调
        //String orderNumber = generateOrderNumber(30);
        String orderNumber = generateNonceStr();
        log.info("生成的订单号：{}", orderNumber);
        request.setOutTradeNo(orderNumber);
        PrepayWithRequestPaymentResponse response = jsService.prepayWithRequestPayment(request);
        log.info("JSApi支付参数：{}，响应：{}", request, response);

        return ApiResponse.success(response);
    }


    public static String generateOrderNumber(int size) {
        String abc = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //指定长度size = 30
        //指定取值范围 abc 如果不指定取值范围，中文环境下会乱码
        String str = RandomStringUtils.random(size, abc);
        return str;
    }

    //public SignInfo generateWXSigner(String prepayid){
    //    String privateKeyStr = WXPayConstants.PRIVATE_KEY_PATH;
    //    PrivateKey key = PemUtil.loadPrivateKeyFromString(privateKeyStr);
    //
    //    //拿到 预支付ID（prepayid）值
    //    //假如-预支付ID：prepayid = "woLD34lk34lk345l345jl345j3l4534ok"
    //    //假如-小程序ID：appid= "applasdiasdfljsf"
    //    //假如-证书序列号：weixinMerchantSerialNumber = "ASDF98SDFAS9D8FASD9F8SAFAS9DF8ASDF98ASDF9S8F98"
    //    String packageStr = String.format("prepay_id=%s", prepayid);
    //    SignInfo info = new SignInfo();
    //    info.setAppId(appid);
    //    info.setTimeStamp("" + System.currentTimeMillis());
    //    info.setNonceStr(generateOrderNumber(30));
    //    info.setPackageStr(packageStr);
    //    String str = info.toString();
    //
    //    Signer rsaSigner = new RSASigner(WXPayConstants.MERCHANT_SERIAL_NUMBER, key);
    //    SignatureResult signatureResult = rsaSigner.sign(str);
    //    info.setPaySign(signatureResult.getSign());
    //    return info;
    //}

    protected static final SecureRandom RANDOM = new SecureRandom();

    //生成随机字符串 微信底层的方法，直接copy出来了
    protected static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(RANDOM.nextInt("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length()));
        }
        return new String(nonceChars);
    }
}
