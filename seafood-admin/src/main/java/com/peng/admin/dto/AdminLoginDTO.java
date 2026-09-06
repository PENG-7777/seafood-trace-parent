package com.peng.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 管理员登录请求DTO
 * 接收前端提交的登录账号与密码参数
 */
@Data
public class AdminLoginDTO {

    /**
     * 管理员账号，非空校验
     */
    @NotBlank(message = "管理员账号不能为空")
    private String username;

    /**
     * 登录密码，非空校验
     */
    @NotBlank(message = "登录密码不能为空")
    private String password;
}

