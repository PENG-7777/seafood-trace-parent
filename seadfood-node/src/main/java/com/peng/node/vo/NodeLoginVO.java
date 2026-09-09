package com.peng.node.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 企业登录接收参数VO
 * 用于接收前端POST提交的JSON登录数据
 */
@Data
public class NodeLoginVO {
    /**
     * 企业登录编码code，作为账号使用
     * NotBlank校验：不能为空、不能全空格
     */
    @NotBlank(message = "登录编码不能为空")
    private String code;

    /**
     * 前端传入明文密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
