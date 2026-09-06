package com.peng.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 管理员实体类
 * 对应数据库表：admin
 */
@Data
@TableName("admin")
public class Admin {

    /**
     * 管理员ID，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer adminId;

    /**
     * 管理员登录账号
     */
    private String username;

    /**
     * 管理员登录密码(BCrypt加密存储，密文60位)
     */
    private String password;

    /**
     * 管理员真实姓名
     */
    private String realName;

}
