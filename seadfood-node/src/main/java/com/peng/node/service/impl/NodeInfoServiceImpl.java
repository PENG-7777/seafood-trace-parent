package com.peng.node.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peng.node.entity.NodeInfo;
import com.peng.node.exception.BusinessException;
import com.peng.node.mapper.NodeInfoMapper;
import com.peng.node.service.NodeInfoService;
import com.peng.node.util.JwtUtil;
import com.peng.node.util.ResultCode;
import com.peng.node.vo.NodeLoginVO;
import com.peng.node.vo.UpdatePwdVO;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 节点企业业务实现 MyBatis‑Plus
 * ServiceImpl 自带baseMapper，提供单表CRUD
 */
@Service
public class NodeInfoServiceImpl extends ServiceImpl<NodeInfoMapper, NodeInfo> implements NodeInfoService {

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 企业登录逻辑
     * 1. QueryWrapper 根据code查询企业
     * 2. BCrypt比对密码
     * 3. 生成JWT token返回
     * @param loginVO 登录表单参数
     * @return token、nodeId、nodeType、nodeName
     */
    @Override
    public Map<String, Object> login(NodeLoginVO loginVO) {
        // MyBatis‑Plus条件构造器：根据登录编码code查询企业
        QueryWrapper<NodeInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("code", loginVO.getCode());
        NodeInfo nodeInfo = this.getOne(wrapper);

        if(nodeInfo == null){
            throw new BusinessException(ResultCode.LOGIN_WRONG);
        }
        // 密码比对：前端明文 vs 数据库密文
        boolean match = passwordEncoder.matches(loginVO.getPassword(), nodeInfo.getPassword());
        if(!match){
            throw new BusinessException(ResultCode.LOGIN_WRONG);
        }
        // 生成token，载荷存放nodeId、企业类型
        String token = jwtUtil.generateToken(nodeInfo.getNodeId(), nodeInfo.getType());
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("token",token);
        resultMap.put("nodeId",nodeInfo.getNodeId());
        resultMap.put("nodeType",nodeInfo.getType());
        resultMap.put("nodeName",nodeInfo.getName());
        return resultMap;
    }

    /**
     * 修改企业密码
     * 1.校验旧密码是否正确
     * 2.校验新密码与确认密码一致
     * 3.BCrypt加密新密码，使用MP updateById更新
     * @param nodeId 当前登录企业id
     * @param updatePwdVO 修改密码参数
     */
    @Override
    public void updatePassword(Integer nodeId, UpdatePwdVO updatePwdVO) {
        NodeInfo nodeInfo = this.getById(nodeId);
        if(nodeInfo == null){
            throw new RuntimeException("企业信息不存在");
        }
        // 校验旧密码
        if(!passwordEncoder.matches(updatePwdVO.getOldPwd(), nodeInfo.getPassword())){
            throw new RuntimeException("旧密码输入错误");
        }
        // 校验新密码和确认密码一致
        if(!updatePwdVO.getNewPwd().equals(updatePwdVO.getConfirmPwd())){
            throw new RuntimeException("两次输入的新密码不一致");
        }
        // 加密新密码
        String newEncryptPwd = passwordEncoder.encode(updatePwdVO.getNewPwd());
        NodeInfo updateEntity = new NodeInfo();
        updateEntity.setNodeId(nodeId);
        updateEntity.setPassword(newEncryptPwd);
        // MyBatis‑Plus 根据id更新，只更新非null字段
        this.updateById(updateEntity);
    }
}
