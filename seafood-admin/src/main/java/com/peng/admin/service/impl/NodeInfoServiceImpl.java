package com.peng.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peng.admin.dto.NodeSaveDTO;
import com.peng.admin.entity.NodeInfo;
import com.peng.admin.mapper.NodeInfoMapper;
import com.peng.admin.service.NodeInfoService;
import com.peng.admin.vo.NodeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 流通节点企业业务实现类
 * 继承MyBatis-Plus ServiceImpl，内置baseMapper，无需手动注入
 */
@Service
public class NodeInfoServiceImpl extends ServiceImpl<NodeInfoMapper, NodeInfo> implements NodeInfoService {

    /**
     * BCrypt密码加密工具
     */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 新增企业默认初始密码
     */
    private static final String DEFAULT_INIT_PASSWORD = "123456";

    @Override
    public IPage<NodeVO> getNodePage(Long pageNum, Long pageSize, String name, Integer type, Integer provId, Integer status) {
        // 构建MP标准分页对象
        Page<NodeVO> page = new Page<>(pageNum, pageSize);
        // 调用baseMapper执行自定义多表分页查询
        return baseMapper.selectNodePage(page, name, type, provId, status);
    }

    @Override
    public void saveOrUpdateNode(NodeSaveDTO dto) {
        NodeInfo nodeInfo = new NodeInfo();
        BeanUtils.copyProperties(dto, nodeInfo);

        if (dto.getNodeId() == null) {
            // 新增场景：设置加密默认密码 + 默认待审核状态
            nodeInfo.setPassword(passwordEncoder.encode(DEFAULT_INIT_PASSWORD));
            nodeInfo.setStatus(1);
            this.save(nodeInfo);
        } else {
            // 编辑场景：MP默认更新策略为 NOT_NULL，null 字段不会被更新到数据库
            // 密码字段不在DTO中，复制后为null，自动忽略更新；status为空也自动忽略
            this.updateById(nodeInfo);
        }
    }

    @Override
    public NodeVO getNodeDetailById(Integer nodeId) {
        // 调用baseMapper执行自定义关联查询
        return baseMapper.selectNodeDetailById(nodeId);
    }

    @Override
    public void updateStatus(Integer nodeId, Integer status) {
        // 使用MP LambdaUpdateWrapper，仅更新status字段，避免全表更新风险
        LambdaUpdateWrapper<NodeInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(NodeInfo::getNodeId, nodeId)
                .set(NodeInfo::getStatus, status);
        this.update(updateWrapper);
    }
}
