package com.nursery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nursery.module.**.mapper")
public class NurseryApplication {
    public static void main(String[] args) {
        SpringApplication.run(NurseryApplication.class, args);
    }
}
