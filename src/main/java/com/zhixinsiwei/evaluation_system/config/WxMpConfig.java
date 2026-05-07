package com.zhixinsiwei.evaluation_system.config;

import com.zhixinsiwei.evaluation_system.common.constant.WXPayConstants;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName WxMpConfig.java
 * @Description TODO
 * @createTime 2026年01月29日 21:40:00
 */
@Configuration
public class WxMpConfig {

    @Bean
    public WxMpService wxMpService() {
        WxMpService service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(WXPayConstants.APPID);
        config.setSecret(WXPayConstants.APP_SECRET);
        service.setWxMpConfigStorage(config);
        return service;
    }
}

