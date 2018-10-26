package com.leyou.auth.service.impl;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.IAuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.item.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 22 20:02
 **/
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties props;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    /**
     * 完成登陆校验并生成token密钥返回
     * @param username
     * @param password
     * @return
     */
    @Override
    public String authentication(String username, String password) {
        try {
            // 查询用户信息
            User user = userClient.queryUser(username, password);
            if (user == null) {
                logger.info("用户信息不存在。{}",username);
                return null;
            }
            // 生成token
            return JwtUtils.generateToken(new UserInfo(user.getId(),user.getUsername()),props.getPrivateKey(),props.getExpire());
        } catch (Exception e) {
            logger.error("出错了",e);
            return null;
        }
    }
}
