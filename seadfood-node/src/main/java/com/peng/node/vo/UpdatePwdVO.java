package com.peng.node.vo;

import lombok.Data;

/**
 * 修改密码VO
 */
@Data
public class UpdatePwdVO {
    /**
     * 旧密码
     */
    private String oldPwd;
    /**
     * 新密码
     */
    private String newPwd;
    /**
     * 确认新密码
     */
    private String confirmPwd;
}
