package com.leyou.user.filter;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.user.config.JwtProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description : 编写springMVC拦截器，进行统一校验登陆
 * @Author santu
 * @Date 2018 - 10 - 23 17:34
 **/
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private JwtProperties props;

    /**
     * 定义threadLocal存放用户信息，同一个线程共享
     */
    private static final ThreadLocal<UserInfo> threadLocal = new ThreadLocal();

    /**
     * 提供全参构造
     * @param props
     */
    public LoginInterceptor(JwtProperties props) {
        this.props = props;
    }

    /**
     * 实现拦截的具体方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getRequestURI());
        // 获得cookie中的token
        String token = CookieUtils.getCookieValue(request, this.props.getCookieName());
        System.out.println("token = " + token);
        if (StringUtils.isNotBlank(token)) {
            try {
                UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.props.getPublicKey());
                // 验证有效，将用户信息存入threadLocal中
                System.out.println(userInfo);
                threadLocal.set(userInfo);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                // 验证无效，返回401
                response.setStatus(401);
                return false;
            }
        }
        return true;
    }

    /**
     * 完成拦截后的处理,移除threadLocal中的信息
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        threadLocal.remove();
    }

    /**
     * 对外提供一个获取当前线程登陆的用户信息的静态方法
     * @return
     */
    public static UserInfo getLoginUser(){
        return threadLocal.get();
    }
}
