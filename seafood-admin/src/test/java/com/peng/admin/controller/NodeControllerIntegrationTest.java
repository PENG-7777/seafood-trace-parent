package com.peng.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.admin.dto.NodeSaveDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NodeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 新增企业接口，默认密码
     */
    @Test
    @Transactional
    @Rollback
    void testSaveNodeApi() throws Exception {
        NodeSaveDTO dto = new NodeSaveDTO();
        dto.setCode("TESTAPI001");
        dto.setName("接口测试-冷冻加工企业");
        dto.setType(3);
        dto.setProvId(19);
        dto.setCityId(38);
        dto.setAddress("汕头市测试地址");
        dto.setBusinessId("91440512MAXXXXAPI1");
        dto.setFoodBusinessLic("SPJYTEST01");
        dto.setCorporation("李四");
        dto.setTelephone("13900139000");

        mockMvc.perform(post("/api/admin/node/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401));
    }


    /**
     * 删除企业接口
     */
    @Test
    @Transactional
    @Rollback
    void testNodeDeleteApi() throws Exception {
        //先新增
        NodeSaveDTO dto = new NodeSaveDTO();
        dto.setCode("TESTAPI002");
        dto.setName("待删除测试企业");
        dto.setType(5);
        dto.setBusinessId("91440512MAXXXXAPI2");
        String result = mockMvc.perform(post("/api/admin/node/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        //删除
        mockMvc.perform(delete("/api/admin/node/delete/" + dto.getNodeId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401));
    }
}
