package com.peng.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.peng.admin.mapper")
public class SeafoodAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeafoodAdminApplication.class, args);
    }

}
