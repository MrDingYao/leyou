package com.leyou.user.service.impl;

import com.leyou.common.utils.CodeUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.item.pojo.User;
import com.leyou.item.pojo.UserInfo;
import com.leyou.user.filter.LoginInterceptor;
import com.leyou.user.mapper.UserInfoMapper;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 21 15:31
 **/
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String KEY_FIX = "user:code:phone:";

    /**
     * 校验用户输入的用户名或者手机号是否已被注册
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
        }
        return this.userMapper.selectCount(user) == 0;
    }

    /**
     * 接收前台传递的手机号，发送验证码
     * @param phone
     * @return
     */
    @Override
    public Boolean sendVerifyCode(String phone) {
        String code = NumberUtils.generateCode(6);
        try {
            // 使用common包中的工具类生成6位的验证码
            Map<String, String> msg = new HashMap<>();
            msg.put("phone", phone);
            msg.put("code", code);
            // 发送短信验证码的rabbitMQ信息
            this.amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);
            // 将手机号和验证码存入redis中，并设置生命周期为5分钟
            this.redisTemplate.opsForValue().set(KEY_FIX + phone, code,10, TimeUnit.MINUTES);

            return true;
        } catch (Exception e) {
            LOGGER.error("发送验证码失败,手机：{}，验证码：{}",phone,code,e);
            return false;
        }
    }

    /**
     * 注册功能，接收用户填写的信息和验证码，完成注册
     * @param user
     * @param code
     * @return
     */
    @Override
    public Boolean register(User user, String code) {
        // 从redis中取出验证码进行校验
        String record = this.redisTemplate.opsForValue().get(KEY_FIX + user.getPhone());
        if (record == null || !record.equals(code)) {
            return false;
        }
        // 验证码校验通过，保存用户数据
        user.setCreated(new Date());
        // 使用工具类生成salt盐
        String salt = CodeUtils.generatorSalt();
        user.setSalt(salt);
        // 使用工具类对密码进行MD5加密
        try {
            String encodePwd = CodeUtils.encodeByMd5(user.getPassword(), salt);
            user.setPassword(encodePwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean boo = this.userMapper.insertSelective(user) == 1;
        // 保存完毕后删除redis中的缓存
        if (boo) {
            try {
                this.redisTemplate.delete(KEY_FIX + user.getPhone());
            } catch (Exception e) {
                LOGGER.error("删除缓存验证码失败。验证码：{}",code,e);
            }
        }
        return boo;
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User queryUser(String username, String password) {
        // 先按照用户名查询校验用户名
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (user == null) {
            return null;
        }
        // 然后验证密码
        if (!user.getPassword().equals(CodeUtils.encodeByMd5(password, user.getSalt()))) {
            return null;
        }
        // 用户名和密码都正确
        return user;
    }

    /**
     * 查询当前用户绑定的手机号
     * @return
     */
    @Override
    public String queryUserPhone() {
        User user = this.userMapper.selectByPrimaryKey(LoginInterceptor.getLoginUser().getId());
        return user.getPhone();
    }

    /**
     * 校验用户输入的验证码
     * @param code
     * @return
     */
    @Override
    public Boolean checkVerifyCode(String phone,String code) {
        // 从redis中取出验证码进行校验
        String record = this.redisTemplate.opsForValue().get(KEY_FIX + phone);
        return record != null && record.equals(code);
    }

    /**
     * 查询用户的个人信息
     * @return
     */
    @Override
    public UserInfo queryUserInfo(Long id) {
        return this.userInfoMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新用户的个人信息
     * @param userInfo
     */
    @Override
    public void updateUserInfo(UserInfo userInfo) {
        userInfo.setUserId(LoginInterceptor.getLoginUser().getId());
        // 先查询该用户是否存过个人信息
        UserInfo record = this.queryUserInfo(LoginInterceptor.getLoginUser().getId());
        // 没存过,则新增
        if (record == null) {
            this.userInfoMapper.insert(userInfo);
        } else {
            // 存过,则更新
            this.userInfoMapper.updateByPrimaryKeySelective(userInfo);
        }
    }

    /**
     * 修改用户绑定的手机
     * @param newPhone
     * @return
     */
    @Override
    public Boolean changePhone(String newPhone) {
        // 先判断该手机是否被绑定
        User user = new User();
        user.setPhone(newPhone);
        List<User> users = this.userMapper.select(user);
        if (!CollectionUtils.isEmpty(users)){
            return null;
        }
        // 未被绑定,则修改手机
        user.setId(LoginInterceptor.getLoginUser().getId());
        int i = this.userMapper.updateByPrimaryKeySelective(user);
        return i == 1;
    }

    /**
     * 修改用户的密码
     * @param newPassword
     * @return
     */
    @Override
    public Boolean changePassword(String newPassword) {
        // 查询当前的用户
        User user = this.userMapper.selectByPrimaryKey(LoginInterceptor.getLoginUser().getId());
        // 使用工具类生成salt盐
        String salt = CodeUtils.generatorSalt();
        user.setSalt(salt);
        // 使用工具类对密码进行MD5加密
        try {
            String encodePwd = CodeUtils.encodeByMd5(newPassword, salt);
            user.setPassword(encodePwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean boo = this.userMapper.updateByPrimaryKeySelective(user) == 1;
        // 保存完毕后删除redis中的缓存
        if (boo) {
            try {
                this.redisTemplate.delete(KEY_FIX + user.getPhone());
            } catch (Exception e) {
                LOGGER.error("删除缓存验证码失败.",e);
            }
        }
        return boo;
    }

    @Override
    public UserInfo queryUserInfo() {
        return this.userInfoMapper.selectByPrimaryKey(LoginInterceptor.getLoginUser().getId());
    }
}
