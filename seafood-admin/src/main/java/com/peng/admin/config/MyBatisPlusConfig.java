package com.peng.admin.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis‑Plus配置类
 * 主要注册分页插件，实现Page对象分页查询功能
 * 如果不配置该插件，MyBatis‑Plus的page分页不会生效，会查询全部数据
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 注册MyBatis‑Plus拦截器
     * 添加分页内部拦截器，数据库类型选择MySQL
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件，指定数据库为MySQL
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置最大单页限制数量，防止恶意传入超大pageSize，-1代表不限制
        paginationInnerInterceptor.setMaxLimit(1000L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}
