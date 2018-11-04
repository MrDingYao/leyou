package com.leyou.item.api;

import com.leyou.item.pojo.User;
import com.leyou.item.pojo.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 22 20:19
 **/
public interface UserApi {

    /**
     * 接收用户名和密码查询用户输入是否正确，完成登陆
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    User queryUser(@RequestParam("username") String username,
                   @RequestParam("password") String password);

    /**
     * 查询用户的个人信息
     * @return
     */
    @GetMapping("info")
    ResponseEntity<UserInfo> queryUserInfo(@RequestParam("id") Long id);

}