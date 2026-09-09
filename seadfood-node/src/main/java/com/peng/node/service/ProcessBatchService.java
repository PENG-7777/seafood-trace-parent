package com.peng.node.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.peng.node.entity.ProcessBatch;
import com.peng.node.vo.SourceBatchVO;
import com.peng.node.vo.WholApplyVO;

import java.util.List;

/**
 * 冷冻加工企业批号业务接口
 * 上游来源：fish_batch（捕捞企业） / farm_sea_batch（海水养殖企业）
 */
public interface ProcessBatchService extends IService<ProcessBatch> {

    /**
     * 查询当前冷冻加工企业自己的批号列表
     * @param nodeId 当前登录冷冻加工企业id
     * @return 加工批号集合
     */
    List<ProcessBatch> getMyBatchList(Integer nodeId);

    /**
     * 新增冷冻加工批号
     * @param processBatch 加工批号表单实体
     * @param nodeId 当前登录企业id
     */
    void addBatch(ProcessBatch processBatch, Integer nodeId);

    /**
     * 更新冷冻加工批号；仅【待发布】状态允许更新
     * @param processBatch 表单实体
     * @param nodeId 当前登录企业id
     */
    void updateBatch(ProcessBatch processBatch, Integer nodeId);

    /**
     * 删除批号；仅【待发布】状态允许删除
     * @param pbId 加工批号主键
     * @param nodeId 当前登录企业id
     */
    void deleteBatch(Integer pbId, Integer nodeId);

    /**
     * 批号下架：修改状态为4‑已下架
     * @param pbId 加工批号主键
     * @param nodeId 当前登录企业id
     */
    void offShelve(Integer pbId, Integer nodeId);

    /**
     * 向上游源头（捕捞/海水养殖）发送进场确认请求，状态修改为2‑待确认
     * @param pbId 加工批号主键
     * @param nodeId 当前登录冷冻加工企业id
     */
    void sendConfirmRequest(Integer pbId, Integer nodeId);

    /**
     * 冷冻加工企业确认批发商进场请求
     * 事务：更新whol_batch批发商批号状态为已确认
     * @param wholBatchId 批发商批号主键
     * @param nodeId 当前登录冷冻加工企业id（权限校验）
     */
    void confirmWholBatch(Integer wholBatchId, Integer nodeId);

    /**
     * 根据上游企业ID，查询该企业【已发布state=2】的原料批号下拉数据
     * 前端新增/编辑页面二级联动下拉使用
     * 上游表：fish_batch（捕捞）、farm_sea_batch（海水养殖）
     * @param sourceNodeId 上游捕捞/海水养殖企业编号
     * @return 上游原料批号VO列表
     */
    List<SourceBatchVO> getUpstreamBatchByNodeId(Integer sourceNodeId);

    /**
     * 查询当前冷冻加工企业作为上游，收到的所有批发商进场申请
     * @param nodeId 当前登录冷冻加工企业ID
     * @return 进场申请VO列表
     */
    List<WholApplyVO> getWholApplyList(Integer nodeId);

}
