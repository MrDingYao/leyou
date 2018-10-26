package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 20 16:47
 **/
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.user.mapper")
public class LyUserService {

    public static void main(String[] args) {
        SpringApplication.run(LyUserService.class, args);
    }
}
