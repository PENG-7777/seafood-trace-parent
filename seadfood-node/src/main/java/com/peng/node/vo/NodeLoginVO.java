package com.peng.node.vo;

import lombok.Data;

/**
 * 企业登录接收参数VO
 */
@Data
public class NodeLoginVO {
    /**
     * 企业登录编码code
     */
    private String code;
    /**
     * 前端传入密码
     */
    private String password;
}
