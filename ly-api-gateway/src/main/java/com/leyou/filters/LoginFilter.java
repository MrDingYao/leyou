package com.leyou.filters;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 22 21:45
 **/
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties props;

    @Autowired
    private FilterProperties filterProperties;

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    /**
     * 读取白名单，放行指定的路径
     * @return
     */
    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        // 获取请求
        HttpServletRequest request = context.getRequest();
        // 获得请求路径
        String requestURI = request.getRequestURI();
        // 如果不是白名单中的路径，则返回true
        return !isAllowPath(requestURI);
    }

    public boolean isAllowPath(String requestUTI){
        // 初始化结果
        boolean flag = false;
        // 获得白名单中的所有路径
        List<String> allowPaths = filterProperties.getAllowPaths();
        // 遍历集合，
        for (String allowPath : allowPaths) {
            if (requestUTI.startsWith(allowPath)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 具体的实施过滤的方法
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        // 获得上下文
        RequestContext context = RequestContext.getCurrentContext();
        // 从上下文中获得请求
        HttpServletRequest request = context.getRequest();
        // 从请求中获得cookie中的token
        String token = CookieUtils.getCookieValue(request, props.getCookieName());
        try {
            // 判断token是否有效
            JwtUtils.getInfoFromToken(token, props.getPublicKey());

        } catch (Exception e) {
            // 检验出现异常，返回403
            logger.info("检验出现异常",e);
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(403);
        }
        return null;
    }
}
