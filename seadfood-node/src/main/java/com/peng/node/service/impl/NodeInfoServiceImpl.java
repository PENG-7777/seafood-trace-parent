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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 节点企业业务实现 MyBatis‑Plus
 * ServiceImpl 自带baseMapper，提供单表CRUD基础方法
 */
@Slf4j
@Service
public class NodeInfoServiceImpl extends ServiceImpl<NodeInfoMapper, NodeInfo> implements NodeInfoService {

    /**
     * BCrypt密码加密工具，用于密码比对与加密
     */
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * JWT工具类，生成登录令牌
     */
    @Resource
    private JwtUtil jwtUtil;

    @Autowired
    private NodeInfoMapper nodeInfoMapper;

    /**
     * 企业登录逻辑
     * 1. QueryWrapper 根据code查询企业数据
     * 2. BCrypt比对前端明文密码和数据库加密密码
     * 3. 校验通过后生成JWT token返回前端
     * @param loginVO 登录表单参数（code账号、password明文密码）
     * @return token、nodeId、nodeType、nodeName 登录信息
     */
    @Override
    public Map<String, Object> login(NodeLoginVO loginVO) {
        // MyBatis‑Plus条件构造器：根据登录编码code查询企业
        QueryWrapper<NodeInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("code", loginVO.getCode());
        NodeInfo nodeInfo = this.getOne(wrapper);

        // 判断账号是否存在
        if(nodeInfo == null){
            log.error("节点登录失败：登录编码【{}】在数据库不存在", loginVO.getCode());
            throw new BusinessException(ResultCode.LOGIN_WRONG);
        }

        // 密码比对：前端明文 vs 数据库密文
        boolean match = passwordEncoder.matches(loginVO.getPassword(), nodeInfo.getPassword());
        if(!match){
            log.error("节点登录失败：编码【{}】密码校验不匹配", loginVO.getCode());
            throw new BusinessException(ResultCode.LOGIN_WRONG);
        }

        log.info("节点登录成功，企业编码：{}，企业名称：{}",loginVO.getCode(),nodeInfo.getName());
        // 生成token，载荷存放nodeId、企业类型
        String token = jwtUtil.generateToken(nodeInfo.getNodeId(), nodeInfo.getType());

        // 封装返回给前端的数据
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
     * 3.BCrypt加密新密码，使用MP updateById更新数据库
     * @param nodeId 当前登录企业id
     * @param updatePwdVO 修改密码参数：旧密码、新密码、确认密码
     */
    @Override
    public void updatePassword(Integer nodeId, UpdatePwdVO updatePwdVO) {
        // 根据主键查询企业信息
        NodeInfo nodeInfo = this.getById(nodeId);
        if(nodeInfo == null){
            throw new RuntimeException("企业信息不存在");
        }

        // 校验旧密码是否匹配
        if(!passwordEncoder.matches(updatePwdVO.getOldPwd(), nodeInfo.getPassword())){
            throw new RuntimeException("旧密码输入错误");
        }

        // 校验两次输入的新密码是否相同
        if(!updatePwdVO.getNewPwd().equals(updatePwdVO.getConfirmPwd())){
            throw new RuntimeException("两次输入的新密码不一致");
        }

        // 使用BCrypt对新密码加密
        String newEncryptPwd = passwordEncoder.encode(updatePwdVO.getNewPwd());
        NodeInfo updateEntity = new NodeInfo();
        updateEntity.setNodeId(nodeId);
        updateEntity.setPassword(newEncryptPwd);

        // MyBatis‑Plus 根据id更新，只更新非null字段
        this.updateById(updateEntity);
        log.info("企业nodeId={}密码修改完成",nodeId);
    }

    /**
     * 根据下游企业类型获取允许选择的上游企业
     * 3冷冻加工 → 上游：1捕捞，2养殖
     * 4批发商 → 上游：3冷冻加工
     * 5零售商 → 上游：4批发商
     * @param targetNodeType 当前登录企业类型
     * @return 上游企业下拉列表
     */
    @Override
    public List<NodeInfo> getUpstreamNodeList(Integer targetNodeType) {
        QueryWrapper<NodeInfo> wrapper = new QueryWrapper<>();
        switch (targetNodeType) {
            case 3:
                // 冷冻加工：上游捕捞、养殖
                wrapper.in("type", 1, 2);
                break;
            case 4:
                // 批发商：上游冷冻加工
                wrapper.eq("type", 3);
                break;
            case 5:
                // 零售商：上游批发商
                wrapper.eq("type", 4);
                break;
            default:
                throw new RuntimeException("当前企业类型不允许选择上游原料");
        }
        return nodeInfoMapper.selectList(wrapper);
    }
}
