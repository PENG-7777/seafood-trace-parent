package com.peng.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peng.admin.dto.NodeSaveDTO;
import com.peng.admin.entity.NodeInfo;
import com.peng.admin.mapper.NodeInfoMapper;
import com.peng.admin.service.NodeInfoService;
import com.peng.admin.vo.NodeVO;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;



/**
 * 流通节点企业业务实现类
 */
@Service
public class NodeInfoServiceImpl extends ServiceImpl<NodeInfoMapper, NodeInfo> implements NodeInfoService {

    @Resource
    private NodeInfoMapper nodeInfoMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 分页查询企业列表
     */
    @Override
    public IPage<NodeVO> getNodePage(Long pageNum, Long pageSize, String name, Integer type) {
        Page<NodeVO> page = new Page<>(pageNum, pageSize);
        return nodeInfoMapper.selectNodePage(page, name, type);
    }

    /**
     * 新增 / 修改企业
     */
    @Override
    public void saveOrUpdateNode(NodeSaveDTO dto) {
        NodeInfo entity = new NodeInfo();
        // 编辑模式：主键赋值
        if(dto.getNodeId() != null){
            entity = this.getById(dto.getNodeId());
        }

        // 基础字段赋值
        entity.setNodeId(dto.getNodeId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setProvId(dto.getProvId());
        entity.setCityId(dto.getCityId());
        entity.setAddress(dto.getAddress());
        entity.setBusinessId(dto.getBusinessId());
        entity.setFishingLic(dto.getFishingLic());
        entity.setAquacultureLic(dto.getAquacultureLic());
        entity.setFoodBusinessLic(dto.getFoodBusinessLic());
        entity.setCorporation(dto.getCorporation());
        entity.setTelephone(dto.getTelephone());
        entity.setRegDate(dto.getRegDate());
        entity.setRemarks(dto.getRemarks());

        // 密码逻辑：有传入明文密码才加密更新；编辑不传密码则保留原有密码
        if(StringUtils.hasText(dto.getPassword())){
            String encryptPwd = passwordEncoder.encode(dto.getPassword());
            entity.setPassword(encryptPwd);
        }

        this.saveOrUpdate(entity);
    }

    /**
     * 根据ID查询详情VO
     */
    @Override
    public NodeVO getNodeDetailById(Integer nodeId) {
        NodeInfo info = this.getById(nodeId);
        if(info == null){
            return null;
        }
        NodeVO vo = new NodeVO();
        vo.setNodeId(info.getNodeId());
        vo.setCode(info.getCode());
        vo.setName(info.getName());
        vo.setType(info.getType());
        vo.setProvId(info.getProvId());
        vo.setCityId(info.getCityId());
        vo.setAddress(info.getAddress());
        vo.setBusinessId(info.getBusinessId());
        vo.setFishingLic(info.getFishingLic());
        vo.setAquacultureLic(info.getAquacultureLic());
        vo.setFoodBusinessLic(info.getFoodBusinessLic());
        vo.setCorporation(info.getCorporation());
        vo.setTelephone(info.getTelephone());
        vo.setRegDate(info.getRegDate());
        vo.setRemarks(info.getRemarks());

        // 翻译类型中文（分页SQL自动处理，详情手动赋值）
        String typeName = switch (info.getType()){
            case 1 -> "捕捞企业";
            case 2 -> "养殖企业";
            case 3 -> "冷冻加工企业";
            case 4 -> "批发商";
            case 5 -> "零售商";

            default -> "";
        };
        vo.setTypeName(typeName);
        return vo;
    }
}
