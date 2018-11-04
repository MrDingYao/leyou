package com.leyou.user.controller;

import com.leyou.item.pojo.User;
import com.leyou.item.pojo.UserInfo;
import com.leyou.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 21 15:31
 **/
@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 校验用户输入的用户名或者手机号是否已被注册
     * 给user加@Valid注解是为了校验属性值是否符合
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable("type") Integer type){
        Boolean result = this.userService.checkData(data,type);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @PostMapping("send")
    public ResponseEntity<Void> sendCode(@RequestParam("phone") String phone){
        Boolean result = this.userService.sendVerifyCode(phone);
        if (result == null || !result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 接收前台传递的用户信息和验证码，完成注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code){
        Boolean result = this.userService.register(user,code);
        if (result == null || !result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 接收用户名和密码查询用户输入是否正确，完成登陆
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username,
                                                    @RequestParam("password") String password){
        User user = this.userService.queryUser(username,password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * 查询当前用户绑定的手机
     * @return
     */
    @GetMapping("phone")
    public ResponseEntity<String> queryUserPhone(){
        String phone = this.userService.queryUserPhone();
        return ResponseEntity.ok(phone);
    }

    /**
     * 接收用户传递的验证码,校验
     * @param code
     * @return
     */
    @GetMapping("code")
    public ResponseEntity<Void> checkVerifyCode(@RequestParam("phone") String phone,@RequestParam("code") String code){
        Boolean boo = this.userService.checkVerifyCode(phone,code);
        if (boo == null || !boo) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 查询用户的个人信息
     * @return
     */
    @GetMapping("info")
    public ResponseEntity<UserInfo> queryUserInfo(@RequestParam("id") Long id){
        UserInfo userInfo = this.userService.queryUserInfo(id);
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(userInfo);
    }

    /**
     * 查询用户的个人信息
     * @return
     */
    @GetMapping("userInfo")
    public ResponseEntity<UserInfo> queryLoginUserInfo(){
        UserInfo userInfo = this.userService.queryUserInfo();
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(userInfo);
    }

    /**
     * 保存用户个人信息
     * @param userInfo
     * @return
     */
    @PostMapping("info")
    public ResponseEntity<Void> updateUserInfo(@RequestBody UserInfo userInfo){
        this.userService.updateUserInfo(userInfo);
        return ResponseEntity.ok().build();
    }

    /**
     * 修改用户绑定的手机
     * @return
     */
    @PutMapping("phone")
    public ResponseEntity<Boolean> changePhone(@RequestParam("newPhone") String newPhone){
        Boolean boo = this.userService.changePhone(newPhone);
        // 如果是null,则返回402
        if (boo == null) {
            return ResponseEntity.ok(false);
        }
        // 修改失败,返回400
        if (!boo) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).build();
        }
        return ResponseEntity.ok(true);
    }

    /**
     * 修改用户的登陆密码
     * @param newPassword
     * @return
     */
    @PutMapping("password")
    public ResponseEntity<Void> changePassword(@RequestParam("pwd") String newPassword){
        Boolean boo = this.userService.changePassword(newPassword);
        if (boo == null || !boo) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

}
