package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 22 18:40
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LyAuthService {

    public static void main(String[] args) {
        SpringApplication.run(LyAuthService.class, args);
    }
}
