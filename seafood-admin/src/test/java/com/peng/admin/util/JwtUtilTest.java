package com.peng.admin.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JwtUtil单元测试
 * 不修改JwtUtil源码，直接测试生成、校验、解析token
 */
@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 测试生成token，校验token，读取userId、userType
     */
    @Test
    void testCreateAndParseToken() {
        // 测试数据：userId=1，userType=0 系统管理员
        Long testUserId = 1L;
        Integer testUserType = 0;

        // 1.生成token
        String token = jwtUtil.createToken(testUserId, testUserType);
        System.out.println("生成的Token：");
        System.out.println(token);

        // 2.校验token是否合法
        boolean verifyResult = jwtUtil.verifyToken(token);
        System.out.println("token校验结果：" + verifyResult);

        // 3.解析userId
        Long userId = jwtUtil.getUserId(token);
        System.out.println("解析userId：" + userId);

        //4.解析userType
        Integer userType = jwtUtil.getUserType(token);
        System.out.println("解析userType：" + userType);

        //断言校验
        assert verifyResult;
        assert testUserId.equals(userId);
        assert testUserType.equals(userType);
    }

    /**
     * 测试伪造非法token
     */
    @Test
    void testInvalidToken() {
        String fakeToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.fake.fake";
        boolean verify = jwtUtil.verifyToken(fakeToken);
        System.out.println("非法token校验结果：" + verify);
        assert !verify;
    }
}
