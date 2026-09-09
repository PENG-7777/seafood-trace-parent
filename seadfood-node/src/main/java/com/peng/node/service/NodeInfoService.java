package com.peng.node.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.peng.node.entity.NodeInfo;
import com.peng.node.vo.NodeLoginVO;
import com.peng.node.vo.UpdatePwdVO;

import java.util.List;
import java.util.Map;

/**
 * 节点企业业务接口 MyBatis‑Plus
 * IService自带单表基础CRUD
 */
public interface NodeInfoService extends IService<NodeInfo> {

    /**
     * 企业登录校验
     * @param loginVO 登录参数：登录编码、明文密码
     * @return map 存放token、nodeId、nodeType、nodeName
     */
    Map<String,Object> login(NodeLoginVO loginVO);

    /**
     * 修改企业密码
     * @param nodeId 当前登录企业id
     * @param updatePwdVO 修改密码参数：旧密码、新密码、确认密码
     */
    void updatePassword(Integer nodeId, UpdatePwdVO updatePwdVO);

    /**
     * 根据下游企业类型，查询它所有合法上游源头企业
     * @param targetNodeType 当前操作企业类型 3加工 /4批发 /5零售
     * @return 上游企业列表
     */
    List<NodeInfo> getUpstreamNodeList(Integer targetNodeType);

}
