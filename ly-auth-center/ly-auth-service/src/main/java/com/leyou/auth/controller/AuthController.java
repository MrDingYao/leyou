package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.IAuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 授权中心，接收用户的用户名和密码，校验成功后，生成token写入到cookie
 * @Author santu
 * @Date 2018 - 10 - 22 19:57
 **/
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private JwtProperties props;

    /**
     * 登陆授权
     * @param username
     * @param password
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> authentication(@RequestParam("username") String username,
                                               @RequestParam("password") String password,
                                               HttpServletRequest request,
                                               HttpServletResponse response){
        // 完成登陆校验
        String token = this.authService.authentication(username, password);
        if (StringUtils.isBlank(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 将token写入cookie中，并制定httpOnly为true，防止通过js获取和修改
        CookieUtils.setCookie(request,response,props.getCookieName(),token,props.getCookieMaxAge(),null,true);
        return ResponseEntity.ok().build();
    }

    /**
     * 验证用户信息
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue(value = "LY_TOKEN",defaultValue = "") String token,
                                               HttpServletRequest request,
                                               HttpServletResponse response){
        try {
            if (StringUtils.isBlank(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            // 进行校验
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, props.getPublicKey());
            // 校验通过后，需要重置token进行刷新
            String newToken = JwtUtils.generateToken(userInfo, props.getPrivateKey(), props.getExpire());
            // 将新生成的token置入cookie中
            CookieUtils.setCookie(request, response,
                    props.getCookieName(), newToken, props.getCookieMaxAge(),
                    null, true);
            // 设置成功后返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
