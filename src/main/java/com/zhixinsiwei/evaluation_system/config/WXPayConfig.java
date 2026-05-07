package com.zhixinsiwei.evaluation_system.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.partnerpayments.app.AppServiceExtension;
import com.wechat.pay.java.service.payments.h5.H5Service;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.zhixinsiwei.evaluation_system.common.constant.WXPayConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName WXPayConfig.java
 * @Description 微信支付配置类
 * @createTime 2024年12月22日 09:31:00
 */
@Configuration
public class WXPayConfig {

    private RSAAutoCertificateConfig config;

    public RSAAutoCertificateConfig getConfig() {
        return config;
    }

    @PostConstruct
    public void init(){
        config = new RSAAutoCertificateConfig.Builder()
                        .merchantId(WXPayConstants.MERCHANT_ID)
                        .privateKey(WXPayConstants.PRIVATE_KEY)
                        // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                        // .privateKeyFromPath(WXPayConstants.PRIVATE_KEY_PATH)
                        .merchantSerialNumber(WXPayConstants.MERCHANT_SERIAL_NUMBER)
                        .apiV3Key(WXPayConstants.API_V3_KEY)
                        .build();
    }


    @Bean("h5Service")
    public H5Service getH5Service(){
        // H5支付
        return new H5Service.Builder().config(config).build();
    }

    @Bean("jsService")
    public JsapiServiceExtension getJsService(){
        // 微信js支付
        return new JsapiServiceExtension.Builder()
                .config(config)
                .signType("RSA") // 不填则默认为RSA
                .build();
    }

    @Bean("appService")
    public AppServiceExtension getAppService() {
        // App支付
        return new AppServiceExtension.Builder().config(config).build();
    }

    @Bean("NotificationParser")
    public NotificationParser getNotificationParser(){
        // 支付回调的解析器
        return new NotificationParser((NotificationConfig)config);
    }
}
