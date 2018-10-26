package com.leyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @ClassName GlobalCorsConfig
 * @Description TODO
 * @Author santu
 * @Date 2018/9/26 11:02
 * @Version 1.0
 **/
@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        // 创建配置对象
        CorsConfiguration config = new CorsConfiguration();
        // 添加允许的域
        //config.addAllowedOrigin("http://manage.leyou.com");
        config.addAllowedOrigin("*");
        // 是否发送cookie信息
        config.setAllowCredentials(false);
        // 添加允许的请求方法
        config.addAllowedMethod(HttpMethod.OPTIONS);
        config.addAllowedMethod(HttpMethod.POST);
        // 添加允许的头信息
        config.addAllowedHeader("*");
        // 添加拦截路径,拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }
}
