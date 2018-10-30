package com.leyou.user.service;

import com.leyou.item.pojo.User;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 21 15:31
 **/
public interface IUserService {

    /**
     * 校验用户输入的数据是否正确
     * @param data
     * @param type
     * @return
     */
    Boolean checkData(String data, Integer type);

    /**
     * 接收前台传递的手机号，发送验证码
     * @param phone
     * @return
     */
    Boolean sendVerifyCode(String phone);

    /**
     * 注册功能，接收用户填写的信息和验证码，完成注册
     * @param user
     * @param code
     * @return
     */
    Boolean register(User user, String code);

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    User queryUser(String username, String password);


}
