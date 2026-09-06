package com.peng.admin.vo;

import lombok.Data;

/**
 * 管理员登录成功返回VO
 * 返回给前端：管理员ID、账号姓名、JWT令牌
 */
@Data
public class AdminLoginVO {

    /**
     * 管理员主键id
     */
    private Integer adminId;

    /**
     * 管理员账号
     */
    private String username;

    /**
     * 管理员昵称/真实姓名
     */
    private String realName;

    /**
     * JWT身份令牌，后续接口请求放在请求头 Authorization: Bearer xxx
     */
    private String token;
}

