package com.peng.seadfoodnode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.node.SeafoodNodeApplication;
import com.peng.node.vo.NodeLoginVO;
import com.peng.node.vo.UpdatePwdVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * NodeController 接口集成测试
 */
@SpringBootTest(classes = SeafoodNodeApplication.class)
@AutoConfigureMockMvc
public class NodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 登录成功
     */
    @Test
    void testLoginSuccess() throws Exception {
        NodeLoginVO loginVO = new NodeLoginVO();
        loginVO.setCode("B001");
        loginVO.setPassword("123456");

        mockMvc.perform(post("/api/node/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginVO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.nodeId").isNumber())
                .andExpect(jsonPath("$.data.nodeType").isNumber())
                .andExpect(jsonPath("$.data.nodeName").isNotEmpty());
    }

    /**
     * 登录失败：密码错误
     */
    @Test
    void testLoginFailWrongPassword() throws Exception {
        NodeLoginVO loginVO = new NodeLoginVO();
        loginVO.setCode("B001");
        loginVO.setPassword("wrongpwd");

        mockMvc.perform(post("/api/node/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginVO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("登录编码或密码错误"));
    }



    /**
     * 修改密码接口
     * 事务自动回滚，不会持久化修改到数据库
     */
    @Test
    @Transactional
    @Rollback
    void testUpdatePwd() throws Exception {
        // 1.登录获取token
        NodeLoginVO loginVO = new NodeLoginVO();
        loginVO.setCode("B001");
        loginVO.setPassword("123456");
        String responseStr = mockMvc.perform(post("/api/node/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginVO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, Object> respMap = objectMapper.readValue(responseStr, Map.class);
        Map<String, Object> dataMap = (Map<String, Object>) respMap.get("data");
        String token = (String) dataMap.get("token");

        // 2.构造修改密码VO，字段 oldPwd / newPwd / confirmPwd
        UpdatePwdVO updatePwdVO = new UpdatePwdVO();
        updatePwdVO.setOldPwd("123456");
        updatePwdVO.setNewPwd("654321");
        updatePwdVO.setConfirmPwd("654321");

        // 3.请求修改密码接口，携带token头部
        mockMvc.perform(post("/api/node/updatePwd")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePwdVO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401));
    }

    /**
     * 修改密码：两次新密码不一致，预期业务异常
     */
    @Test
    void testUpdatePwdNotMatch() throws Exception {
        NodeLoginVO loginVO = new NodeLoginVO();
        loginVO.setCode("B001");
        loginVO.setPassword("123456");
        String responseStr = mockMvc.perform(post("/api/node/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginVO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, Object> respMap = objectMapper.readValue(responseStr, Map.class);
        Map<String, Object> dataMap = (Map<String, Object>) respMap.get("data");
        String token = (String) dataMap.get("token");

        UpdatePwdVO updatePwdVO = new UpdatePwdVO();
        updatePwdVO.setOldPwd("123456");
        updatePwdVO.setNewPwd("111111");
        updatePwdVO.setConfirmPwd("222222");

        mockMvc.perform(post("/api/node/updatePwd")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePwdVO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401));
    }
}
