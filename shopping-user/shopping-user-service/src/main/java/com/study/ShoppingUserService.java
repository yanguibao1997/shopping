package com.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.study.mapper")
public class ShoppingUserService {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingUserService.class,args);
    }
}
