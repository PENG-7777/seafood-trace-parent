package com.peng.node;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.peng.node")
@MapperScan("com.peng.node.mapper")
public class SeafoodNodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeafoodNodeApplication.class, args);
    }
}
