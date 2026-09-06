package com.peng.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peng.admin.dto.AdminLoginDTO;
import com.peng.admin.entity.Admin;
import com.peng.admin.mapper.AdminMapper;
import com.peng.admin.service.AdminService;
import com.peng.admin.util.JwtUtil;
import com.peng.admin.vo.AdminLoginVO;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * 管理员业务实现类
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Resource
    private JwtUtil jwtUtil;

    /**
     * BCrypt密码加密校验工具
     */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 管理员类型固定值：0 = 系统管理员
     */
    private static final Integer ADMIN_USER_TYPE = 0;

    @Override
    public AdminLoginVO login(AdminLoginDTO loginDTO) {
        // 1. 根据账号查询管理员信息
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, loginDTO.getUsername());
        Admin admin = this.getOne(wrapper);

        // 账号不存在
        if (admin == null) {
            return null;
        }

        // 2. BCrypt比对密码
        boolean match = passwordEncoder.matches(loginDTO.getPassword(), admin.getPassword());
        if (!match) {
            return null;
        }

        // 3. 使用现有JwtUtil.createToken生成令牌
        // adminId(Integer) → 转为Long，userType固定传0代表后台管理员
        String token = jwtUtil.createToken(Long.valueOf(admin.getAdminId()), ADMIN_USER_TYPE);

        // 4. 组装返回VO
        AdminLoginVO vo = new AdminLoginVO();
        vo.setAdminId(admin.getAdminId());
        vo.setUsername(admin.getUsername());
        vo.setRealName(admin.getRealName());
        vo.setToken(token);
        return vo;
    }
}
