package com.peng.seadfoodnode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.node.SeafoodNodeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TraceController 公开溯源查询接口测试
 * 接口无需token，Jwt拦截器已放行
 */
@SpringBootTest(classes = SeafoodNodeApplication.class)
@AutoConfigureMockMvc
public class TraceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 溯源查询：传入有效溯源码 E1300BEFAC
     */
    @Test
    void testQueryTraceSuccess() throws Exception {
        mockMvc.perform(get("/api/public/trace/query")
                        .param("sourceId", "E1300BEFAC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    /**
     * 溯源查询：sourceId为空
     */
    @Test
    void testQueryTraceEmptySourceId() throws Exception {
        mockMvc.perform(get("/api/public/trace/query")
                        .param("sourceId", ""))
                .andExpect(status().isOk());
    }

    /**
     * 溯源查询：传入不存在的溯源码
     */
    @Test
    void testQueryTraceNotExist() throws Exception {
        mockMvc.perform(get("/api/public/trace/query")
                        .param("sourceId", "NOT_EXIST_123456"))
                .andExpect(status().isOk());
    }
}
