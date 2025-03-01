package com.example.familyeducation.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassDescription: 支付宝配置类
 * @Author:小菜
 * @Create:2025/1/6 10:24
 **/

@Configuration
@EnableConfigurationProperties(AliPayConfigProperties.class)
public class AliPayConfig {


    @Bean("alipayClient")
    public AlipayClient alipayClient(AliPayConfigProperties properties){
        return new DefaultAlipayClient(properties.getGatewayUrl(),
                properties.getApp_id(),
                properties.getMerchant_private_key(),
                properties.getFormat(),
                properties.getCharset(),
                properties.getAlipay_public_key(),
                properties.getSign_type()
                );
    }

}
