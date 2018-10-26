package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 23 10:54
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LyCartService {

    public static void main(String[] args) {
        SpringApplication.run(LyCartService.class, args);
    }
}
