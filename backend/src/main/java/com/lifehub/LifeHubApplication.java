package com.lifehub;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lifehub.mapper")
public class LifeHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(LifeHubApplication.class, args);
    }
}
