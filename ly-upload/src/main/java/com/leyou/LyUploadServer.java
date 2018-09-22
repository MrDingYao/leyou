package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName LyUploadServer
 * @Description 上传的模块的启动
 * @Author santu
 * @Date 2018/9/22 21:35
 * @Version 1.0
 **/
@SpringBootApplication
@EnableEurekaClient
public class LyUploadServer {

    public static void main(String[] args) {
        SpringApplication.run(LyUploadServer.class, args);
    }
}
