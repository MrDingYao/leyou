package com.leyou.auth.config;

import com.leyou.auth.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Description JWT加密的一些基本属性设置，初始化公钥和私钥
 * @Author santu
 * @Date 2018 - 10 - 22 19:32
 **/
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {

    private String secret;

    /**
     * 公钥存放路径
     */
    private String pubKeyPath;

    /**
     * 私钥存放路径
     */
    private String priKeyPath;

    /**
     * 有效时间
     */
    private Integer expire;

    /**
     * cookie的过期时间
     */
    private Integer cookieMaxAge;

    /**
     * cookie的名字
     */
    private String cookieName;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 私钥
     */
    private PrivateKey privateKey;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    /**
     * 类加载时的初始化密钥的方法
     */
    @PostConstruct
    public void init() {
        try {
            File pubKeyFile = new File(this.pubKeyPath);
            File priKeyFile = new File(this.priKeyPath);
            // 判断两个密钥是否存在,若不存在，则生成两个密钥
            if (!pubKeyFile.exists() || !priKeyFile.exists()) {
                RsaUtils.generateKey(this.pubKeyPath,this.priKeyPath,this.secret);
            }
            // 若存在两个密钥文件，则读取密钥文件
            this.privateKey = RsaUtils.getPrivateKey(this.priKeyPath);
            this.publicKey = RsaUtils.getPublicKey(this.pubKeyPath);
        } catch (Exception e) {
            this.logger.error("初始化公钥和私钥失败",e);
            throw new RuntimeException();
        }
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public Integer getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
