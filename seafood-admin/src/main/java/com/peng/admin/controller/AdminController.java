package com.peng.admin.controller;

import com.peng.admin.common.Result;
import com.peng.admin.dto.AdminLoginDTO;
import com.peng.admin.service.AdminService;
import com.peng.admin.util.ResultUtil;
import com.peng.admin.vo.AdminLoginVO;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 管理员登录控制器
 * 路径前缀：/api/admin
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 管理员登录接口
     * @param loginDTO 前端提交账号密码
     * @return 登录结果，成功返回管理员信息与JWT令牌
     */
    @PostMapping("/login")
    public Result<AdminLoginVO> login(@RequestBody @Validated AdminLoginDTO loginDTO) {
        AdminLoginVO loginVo = adminService.login(loginDTO);
        if (loginVo == null) {
            // 账号不存在 / 密码错误统一返回相同提示，安全策略
            return ResultUtil.fail("账号或密码错误");
        }
        return ResultUtil.success(loginVo);
    }

}

