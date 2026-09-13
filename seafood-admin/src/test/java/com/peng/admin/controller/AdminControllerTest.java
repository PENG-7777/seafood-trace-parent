package com.peng.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.admin.dto.AdminLoginDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AdminController 测试
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 登录成功：账号密码正确
     */
    @Test
    void testLoginSuccess() throws Exception {
        AdminLoginDTO loginDTO = new AdminLoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("123456");

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("admin"));
    }

    /**
     * 登录失败：密码错误
     */
    @Test
    void testLoginFailWrongPassword() throws Exception {
        AdminLoginDTO loginDTO = new AdminLoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("wrong123");

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("账号或密码错误"));
    }

    /**
     * 参数校验：用户名为空，@Validated 校验失败
     */
    @Test
    void testLoginParamEmptyUsername() throws Exception {
        AdminLoginDTO loginDTO = new AdminLoginDTO();
        loginDTO.setUsername("");
        loginDTO.setPassword("123456");

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }
}
