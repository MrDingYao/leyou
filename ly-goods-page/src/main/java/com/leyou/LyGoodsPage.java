package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description TODO : 商品详情浏览量比较大，并发高，我们会独立开启一个微服务，用来展示商品详情
 * @Author santu
 * @Date 2018 - 10 - 17 16:17
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LyGoodsPage {

    public static void main(String[] args) {
        SpringApplication.run(LyGoodsPage.class, args);
    }
}
