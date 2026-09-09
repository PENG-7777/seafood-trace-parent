package com.peng.node.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.peng.node.entity.WholBatch;
import com.peng.node.vo.RetailApplyVO;
import com.peng.node.vo.SourceBatchVO;

import java.util.List;

/**
 * 批发商批号业务接口
 * 业务链路：上游=冷冻加工企业(process_batch)，下游=零售商(reta_batch)
 */
public interface WholBatchService extends IService<WholBatch> {

    /**
     * 查询当前批发商企业自己的批号列表
     * @param nodeId 当前登录批发商企业id
     * @return 批发商批号集合
     */
    List<WholBatch> getMyBatchList(Integer nodeId);

    /**
     * 新增批发商批号
     * 业务校验：上游冷冻加工批号必须存在并且状态=3已确认；sourceNodeType后端强制赋值3
     * @param wholBatch 批发商批号表单实体
     * @param nodeId 当前登录批发商企业id
     */
    void addBatch(WholBatch wholBatch, Integer nodeId);

    /**
     * 更新批发商批号；仅【1‑新建】状态允许更新
     * @param wholBatch 表单实体
     * @param nodeId 当前登录企业id
     */
    void updateBatch(WholBatch wholBatch, Integer nodeId);

    /**
     * 删除批号；仅【1‑新建】状态允许删除
     * @param wbId 批发商批号主键
     * @param nodeId 当前登录企业id
     */
    void deleteBatch(Integer wbId, Integer nodeId);

    /**
     * 批号下架：修改状态为4‑已下架
     * @param wbId 批发商批号主键
     * @param nodeId 当前登录企业id
     */
    void offShelve(Integer wbId, Integer nodeId);

    /**
     * 向上游冷冻加工企业发送进场确认请求，状态修改为2‑待确认
     * @param wbId 批发商批号主键
     * @param nodeId 当前登录批发商企业id
     */
    void sendConfirmRequest(Integer wbId, Integer nodeId);

    /**
     * 批发商确认零售商进场请求
     * 事务：更新reta_batch零售商批号状态为已确认；状态变更为已确认自动生成溯源标识码sourceId
     * @param retaBatchId 零售商批号主键
     * @param nodeId 当前登录批发商企业id（权限校验）
     */
    void confirmRetaBatch(Integer retaBatchId, Integer nodeId);

    /**
     * 根据上游冷冻加工企业ID，下拉查询该企业【state=3已确认】的加工成品批号
     * 用于新增批发商页面二级联动下拉选择上游原料
     * @param sourceNodeId 上游冷冻加工企业编号
     * @return 上游原料下拉VO列表
     */
    List<SourceBatchVO> getUpstreamProcessBatchByNodeId(Integer sourceNodeId);

    /**
     * 查询提交给当前登录批发商的所有零售商进场申请列表
     * @param nodeId 当前登录批发商企业ID
     * @return 零售商申请VO集合
     */
    List<RetailApplyVO> getRetaApplyList(Integer nodeId);
}
