package com.leyou.sms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 21 16:31
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "ly.sms")
public class SmsProperties {

    /**
     *  密钥
     */
    private String accessKeyId;

    /**
     * 密钥
     */
    private String accessKeySecret;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信模板ID
     */
    private String verifyCodeTemplate;




}
