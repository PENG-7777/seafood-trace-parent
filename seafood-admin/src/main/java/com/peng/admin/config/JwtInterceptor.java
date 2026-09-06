package com.peng.admin.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.admin.common.Result;
import com.peng.admin.common.ResultCode;
import com.peng.admin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT Token登录拦截器
 * 实现HandlerInterceptor接口，在Controller执行之前拦截请求
 * 功能：从请求头获取token，校验token是否合法、是否过期
 * token非法/过期：直接返回JSON未登录提示，不放行进入controller
 * token合法：解析出用户信息存入request域，放行执行业务接口
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    // 注入JwtUtil Spring Bean
    private final JwtUtil jwtUtil;

    // 构造函数注入依赖
    public JwtInterceptor(ObjectMapper objectMapper, JwtUtil jwtUtil) {
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    /**
     * preHandle：controller执行之前执行
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 处理器
     * @return true放行；false拦截请求
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 设置响应返回JSON格式，防止返回html页面
        response.setContentType("application/json;charset=UTF-8");

        // 1.从请求头获取token，约定key为token
        String token = request.getHeader("token");
        if (token == null || token.trim().length() == 0) {
            log.warn("JWT拦截：请求头未携带token");
            // 构造未登录错误Result，写出到响应
            Result<Void> errResult = Result.err(ResultCode.UNAUTHORIZED);
            String json = objectMapper.writeValueAsString(errResult);
            response.getWriter().write(json);
            return false;
        }

        //2.校验token有效性：使用注入的jwtUtil实例对象调用实例方法
        boolean verify = jwtUtil.verifyToken(token);
        if (!verify) {
            log.warn("JWT拦截：token无效或者已过期，token={}", token);
            Result<Void> errResult = Result.err(ResultCode.UNAUTHORIZED);
            String json = objectMapper.writeValueAsString(errResult);
            response.getWriter().write(json);
            return false;
        }

        //3.token合法，解析token，拿到用户id、用户类型，存入request，controller可以获取
        Long userId = jwtUtil.getUserId(token);
        Integer userType = jwtUtil.getUserType(token);
        request.setAttribute("loginUserId", userId);
        request.setAttribute("loginUserType", userType);

        log.info("JWT拦截校验通过，userId={},userType={}", userId, userType);
        //校验通过，放行，进入controller
        return true;
    }
}
