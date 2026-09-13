package com.peng.admin.service;

import com.peng.admin.dto.AdminLoginDTO;
import com.peng.admin.vo.AdminLoginVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdminServiceImplTest {

    @Autowired
    private AdminService adminService;

    @Test
    void testLogin() {
        AdminLoginDTO dto = new AdminLoginDTO();
        dto.setUsername("admin");
        dto.setPassword("123456");
        AdminLoginVO vo = adminService.login(dto);
        System.out.println(vo);
        assertNotNull(vo);
    }
}
