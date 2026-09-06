package com.peng.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.peng.admin.dto.AdminLoginDTO;
import com.peng.admin.entity.Admin;
import com.peng.admin.vo.AdminLoginVO;

/**
 * 管理员业务接口
 */
public interface AdminService extends IService<Admin> {

    /**
     * 管理员登录校验
     * @param loginDTO 登录账号密码参数
     * @return 登录成功返回令牌VO对象
     */
    AdminLoginVO login(AdminLoginDTO loginDTO);
}
