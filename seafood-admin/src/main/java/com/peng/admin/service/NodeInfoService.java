package com.peng.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.peng.admin.dto.NodeSaveDTO;
import com.peng.admin.entity.NodeInfo;
import com.peng.admin.vo.NodeVO;

/**
 * 流通节点企业业务接口
 */
public interface NodeInfoService extends IService<NodeInfo> {

    /**
     * 企业分页列表查询（带省份城市中文名称）
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param name 企业名称模糊关键词
     * @param type 企业类型筛选
     * @return 分页VO
     */
    IPage<NodeVO> getNodePage(Long pageNum, Long pageSize, String name, Integer type);

    /**
     * 新增/编辑保存企业信息
     * @param dto 前端提交保存DTO
     */
    void saveOrUpdateNode(NodeSaveDTO dto);

    /**
     * 根据id查询企业详情VO
     * @param nodeId 企业id
     * @return NodeVO
     */
    NodeVO getNodeDetailById(Integer nodeId);
}

