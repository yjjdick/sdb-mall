package io.sdb.config;

import com.google.common.collect.Maps;
import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yjjdick
 * 2017-07-04 01:05
 */
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WechatPayConfig {

    private WxMaProperties properties;

    public WechatPayConfig(WxMaProperties maProperties) {
        this.properties = maProperties;
    }

    @Bean
    public BestPayServiceImpl bestPayService() {
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayH5Config(wxPayH5Config());
        return bestPayService;
    }

    @Bean
    public WxPayH5Config wxPayH5Config() {
        WxPayH5Config wxPayH5Config = new WxPayH5Config();
        wxPayH5Config.setAppId(this.properties.getAppid());
        wxPayH5Config.setAppSecret(properties.getSecret());
        wxPayH5Config.setMchId(properties.getMchId());
        wxPayH5Config.setMchKey(properties.getMchKey());
        wxPayH5Config.setKeyPath(properties.getKeyPath());
        wxPayH5Config.setNotifyUrl(properties.getNotifyUrl());
        return wxPayH5Config;
    }
}
