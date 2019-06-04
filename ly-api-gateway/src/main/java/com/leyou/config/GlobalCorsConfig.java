package com.leyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @ClassName GlobalCorsConfig
 * @Description spring配置类,用来生成corsfilter过滤器,实现跨域的Ajax请求
 * @Author santu
 * @Date 2018/9/21 15:30
 * @Version 1.0
 **/
@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        // 创建corsFilter的配置
        CorsConfiguration config = new CorsConfiguration();
        // 允许的域,不能写*,否则不能添加cookie
        config.addAllowedOrigin("http://manage.leyou.com");
//        config.addAllowedOrigin("http://www.leyou.com");
        // 是否允许添加cookie
        config.setAllowCredentials(true);
        // 允许的请求方法
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        config.addAllowedMethod(HttpMethod.PATCH);
        config.addAllowedMethod(HttpMethod.OPTIONS);
        config.addAllowedMethod(HttpMethod.HEAD);
        // 允许的头信息
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        // 添加拦截路径,拦截所有
        configSource.registerCorsConfiguration("/**",config);
        // 返回新的CorsFilter
        return new CorsFilter(configSource);
    }
}
