package com.leyou.auth.service;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 22 20:02
 **/
public interface IAuthService {
    /**
     * 完成登陆校验，并生成密钥token返回
     * @param username
     * @param password
     * @return
     */
    String authentication(String username, String password);
}
