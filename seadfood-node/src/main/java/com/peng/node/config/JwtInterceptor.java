package com.peng.node.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.node.util.JwtUtil;
import com.peng.node.util.Result;
import com.peng.node.util.ResultCode;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

/**
 * 企业用户JWT Token拦截器
 * 对所有/api/node下接口做token鉴权，放行登录接口、大屏统计接口 /api/node/stats/dashboard
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * controller执行之前拦截处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 重点：OPTIONS预检请求直接放行，交给跨域配置处理
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();
        System.out.println("当前请求uri：" + uri);

        // ===================== 新增放行判断 =====================
        // 放行大屏统计接口：/api/node/stats/dashboard
        if ("/api/node/stats/dashboard".equals(uri)) {
            return true;
        }
        // ======================================================

        // Token校验逻辑
        String token = request.getHeader("token");
        if (token == null || token.isBlank()) {
            writeUnauthorizedResponse(response);
            return false;
        }
        if (!jwtUtil.validateToken(token)) {
            writeUnauthorizedResponse(response);
            return false;
        }
        Integer nodeId = jwtUtil.getNodeId(token);
        Integer nodeType = jwtUtil.getNodeType(token);
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("nodeType", nodeType);
        return true;
    }

    /**
     * 输出401未登录响应JSON
     */
    private void writeUnauthorizedResponse(HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        Result<?> result = Result.err(ResultCode.UNAUTHORIZED);
        writer.write(objectMapper.writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
