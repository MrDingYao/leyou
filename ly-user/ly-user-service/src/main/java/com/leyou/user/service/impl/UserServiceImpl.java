package com.leyou.user.service.impl;

import com.leyou.common.utils.CodeUtils;
import com.leyou.common.utils.IdWorker;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.item.pojo.User;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            this.redisTemplate.opsForValue().set(KEY_FIX + phone, code,5, TimeUnit.MINUTES);

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

}
