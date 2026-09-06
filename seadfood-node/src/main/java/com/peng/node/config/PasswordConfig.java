package com.peng.node.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密配置类
 * 注册BCryptPasswordEncoder加密Bean，用于企业密码加密与校验
 */
@Configuration
public class PasswordConfig {

    /**
     * 创建BCrypt密码加密器Bean
     * BCrypt算法：加盐哈希，每次加密结果不同，比对使用matches方法，不要直接equals字符串
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
