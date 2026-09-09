package com.peng.node.controller;

import com.peng.node.entity.NodeInfo;
import com.peng.node.service.NodeInfoService;
import com.peng.node.util.Result;
import com.peng.node.vo.NodeLoginVO;
import com.peng.node.vo.UpdatePwdVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 流通节点企业控制器
 * 接口：企业登录、修改密码
 * 登录接口被JwtInterceptor放行无需token；修改密码需要携带token鉴权
 */
@Slf4j
@RestController
@RequestMapping("/api/node")
public class NodeController {

    @Resource
    private NodeInfoService nodeInfoService;

    /**
     * 流通企业登录接口
     * @Valid 开启VO字段非空校验
     * @RequestBody 接收前端JSON格式请求体
     * @param loginVO 登录参数 code登录编码、password明文密码
     * @return Result，data包含token、nodeId、nodeType、nodeName
     */
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@Valid @RequestBody NodeLoginVO loginVO){
        //打印Controller接收到的登录参数，用于调试null问题
        log.info("Controller接收登录参数，code = {}，password = {}", loginVO.getCode(), loginVO.getPassword());
        Map<String, Object> data = nodeInfoService.login(loginVO);
        return Result.ok(data);
    }

    /**
     * 修改企业密码接口
     * 需要token鉴权，从request域获取当前登录nodeId
     * @param updatePwdVO 旧密码、新密码、确认密码
     * @param request http请求对象，获取拦截器存入的登录企业nodeId
     * @return Result 统一返回体，无返回数据
     */
    @PostMapping("/updatePwd")
    public Result<Void> updatePwd(@Valid @RequestBody UpdatePwdVO updatePwdVO, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        nodeInfoService.updatePassword(nodeId, updatePwdVO);
        return Result.ok();
    }

    /**
     * 获取下游企业对应的全部上游企业下拉列表（一级下拉）
     * @param targetNodeType 当前登录企业类型 3/4/5
     * @return 上游企业集合
     */
    @GetMapping("/getUpstreamNodeList/{targetNodeType}")
    public Result<List<NodeInfo>> getUpstreamNodeList(@PathVariable Integer targetNodeType) {
        List<NodeInfo> list = nodeInfoService.getUpstreamNodeList(targetNodeType);
        return Result.ok(list);
    }
}
