package com.peng.node.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.peng.node.entity.RetaBatch;
import java.util.List;

/**
 * 零售商批号业务接口层
 */
public interface RetaBatchService extends IService<RetaBatch> {

    /**
     * 查询当前登录零售商自身的批号列表
     * @param nodeId 当前登录零售商企业id
     * @return 零售商批号集合
     */
    List<RetaBatch> getMyBatchList(Integer nodeId);

    /**
     * 新增零售商批号
     * 业务校验：上游批发商批号必须存在并且状态=3已确认
     * @param retaBatch 零售商批号表单实体
     * @param nodeId 当前登录零售商企业id
     */
    void addBatch(RetaBatch retaBatch, Integer nodeId);

    /**
     * 修改零售商批号；仅【1‑新建】状态允许更新
     * @param retaBatch 表单实体
     * @param nodeId 当前登录企业id
     */
    void updateBatch(RetaBatch retaBatch, Integer nodeId);

    /**
     * 删除零售商批号；仅【1‑新建】状态允许删除
     * @param rbId 零售商批号主键
     * @param nodeId 当前登录企业id
     */
    void deleteBatch(Integer rbId, Integer nodeId);

    /**
     * 批号下架：修改状态为4‑已下架
     * @param rbId 零售商批号主键
     * @param nodeId 当前登录企业id
     */
    void offShelve(Integer rbId, Integer nodeId);

    /**
     * 零售商向上游批发商发送进场确认请求，状态修改为2‑待确认
     * @param rbId 零售商批号主键
     * @param nodeId 当前登录零售商企业id
     */
    void sendConfirmRequest(Integer rbId, Integer nodeId);
}
