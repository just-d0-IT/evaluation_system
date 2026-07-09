package com.zhixinsiwei.evaluation_system.service.impl;

import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.Transaction;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.zhixinsiwei.evaluation_system.common.ApiResponse;
import com.zhixinsiwei.evaluation_system.common.constant.WXPayConstants;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationRecords;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationUsers;
import com.zhixinsiwei.evaluation_system.config.WXPayConfig;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.RecordService;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.UserEntityService;
import com.zhixinsiwei.evaluation_system.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Resource
    private WXPayConfig wxPayConfig;

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
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!StringUtils.hasText(user.getUserKey())) {
            throw new RuntimeException("用户未完成微信授权");
        }
        if (record == null) {
            throw new RuntimeException("记录不存在");
        }
        if (!record.getUserId().equals(userId)) {
            throw new RuntimeException("无权支付该记录");
        }
        if (!StringUtils.hasText(record.getReport())) {
            throw new RuntimeException("报告分析中，请稍后支付");
        }
        if (record.getPayStatus() != null && record.getPayStatus() == 1) {
            throw new RuntimeException("报告已支付");
        }
        log.info("user is {},record is {}", user, record);
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
        request.setDescription("智商测试国际版公众号支付"); //详情
        request.setNotifyUrl(WXPayConstants.NOTIFY_URL);    //回调
        // 订单号使用 recordId + 时间戳（base36压缩），保证每次下单唯一且不超过微信32字符限制，
        // 避免"请求重入参数不一致"；回调时以下划线前缀反查 recordId
        String outTradeNo = recordId + "_" + Long.toString(System.currentTimeMillis(), 36);
        log.info("生成订单号：{}（recordId：{}）", outTradeNo, recordId);
        request.setOutTradeNo(outTradeNo);
        PrepayWithRequestPaymentResponse response = jsService.prepayWithRequestPayment(request);
        log.info("JSApi支付参数：{}，响应：{}", request, response);

        return ApiResponse.success(response);
    }


    @Override
    public int handleNotify(String body, String serialNumber, String nonce, String signature, String timestamp) {
        log.info("=========微信支付异步回调开始========");
        try {
            // 构造 RequestParam
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(serialNumber)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timestamp)
                    .body(body)
                    .build();
            // 初始化解析器
            NotificationParser parser = new NotificationParser(wxPayConfig.getConfig());
            // 验签、解密并转换成 Transaction
            Transaction transaction = parser.parse(requestParam, Transaction.class);
            // 校验交易状态
            if (Transaction.TradeStateEnum.SUCCESS.equals(transaction.getTradeState())) {
                // 支付成功，outTradeNo格式为 recordId_时间戳，取下划线前缀反查recordId
                String outTradeNo = transaction.getOutTradeNo();
                String recordId = outTradeNo.contains("_") ? outTradeNo.split("_")[0] : outTradeNo;
                log.info("支付成功，outTradeNo：{}，recordId：{}", outTradeNo, recordId);
                EvaluationRecords record = recordService.getById(recordId);
                if (record != null) {
                    // 幂等处理：已支付则直接返回成功，避免重复更新（微信可能重复推送通知）
                    if (record.getPayStatus() != null && record.getPayStatus() == 1) {
                        log.info("记录已支付，跳过更新，recordId：{}", recordId);
                        return 0;
                    }
                    // 校验金额是否一致（微信回调金额单位为分，需转换对比）
                    int wxAmount = transaction.getAmount().getTotal();
                    int dbAmount = new BigDecimal(record.getPayPrice()).multiply(new BigDecimal("100")).intValue();
                    if (wxAmount != dbAmount) {
                        log.error("支付金额不一致，recordId：{}，微信回调金额：{}分，数据库记录金额：{}分", recordId, wxAmount, dbAmount);
                        return 2;
                    }
                    record.setPayStatus(1); // 1-已支付
                    recordService.updateById(record);
                    log.info("更新记录支付状态成功，recordId：{}", recordId);
                } else {
                    log.error("支付回调未找到对应记录，recordId：{}", recordId);
                    return 2;
                }
            }
            log.info("transaction is {}", transaction);
        } catch (ValidationException e) {
            log.error("微信支付回调签名验证失败", e);
            return 1;
        } catch (Exception e) {
            log.error("微信支付回调处理异常", e);
            return 2;
        }
        log.info("=========微信支付异步回调结束========");
        return 0;
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
