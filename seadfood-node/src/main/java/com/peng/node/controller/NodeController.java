package com.peng.node.controller;

import com.peng.node.service.NodeInfoService;
import com.peng.node.util.Result;
import com.peng.node.vo.NodeLoginVO;
import com.peng.node.vo.UpdatePwdVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

/**
 * 流通节点企业控制器
 * 接口：企业登录、修改密码
 * 登录接口被JwtInterceptor放行无需token；修改密码需要携带token鉴权
 */
@RestController
@RequestMapping("/api/node")
public class NodeController {

    @Resource
    private NodeInfoService nodeInfoService;

    /**
     * 流通企业登录接口
     * @param loginVO 登录参数 code登录编码、password明文密码
     * @return Result，data包含token、nodeId、nodeType、nodeName
     */
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody NodeLoginVO loginVO){
        Map<String, Object> data = nodeInfoService.login(loginVO);
        return Result.ok(data);
    }

    /**
     * 修改企业密码接口
     * 需要token鉴权，从request域获取当前登录nodeId
     * @param updatePwdVO 旧密码、新密码、确认密码
     * @param request http请求对象，获取登录企业nodeId
     * @return Result
     */
    @PostMapping("/updatePwd")
    public Result<Void> updatePwd(@RequestBody UpdatePwdVO updatePwdVO, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        nodeInfoService.updatePassword(nodeId, updatePwdVO);
        return Result.ok();
    }
}
