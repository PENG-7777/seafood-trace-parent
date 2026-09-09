package com.peng.node.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.peng.node.entity.FarmSeaBatch;
import com.peng.node.entity.ProcessBatch;

import java.util.List;

/**
 * 海水养殖企业批号业务接口
 */
public interface FarmSeaBatchService extends IService<FarmSeaBatch> {

    /**
     * 查询当前养殖企业自己的批号列表
     * @param nodeId 当前登录养殖企业id
     * @return 批号集合
     */
    List<FarmSeaBatch> getMyBatchList(Integer nodeId);

    /**
     * 批号下架：修改状态为3‑已下架
     * @param fsbId 养殖批号主键
     * @param nodeId 当前登录企业id（数据权限校验）
     */
    void offShelve(Integer fsbId, Integer nodeId);

    /**
     * 删除批号；仅【待发布】状态(state=1)允许删除
     * @param fsbId 养殖批号主键
     * @param nodeId 当前登录企业id（数据权限校验）
     */
    void deleteBatch(Integer fsbId, Integer nodeId);

    /**
     * 更新养殖批号；仅【待发布】状态允许更新
     * @param farmSeaBatch 批号表单数据
     * @param nodeId 当前登录企业id
     */
    void updateBatch(FarmSeaBatch farmSeaBatch, Integer nodeId);

    /**
     * 新增海水养殖企业批号
     * @param farmSeaBatch 批号实体
     * @param nodeId 当前登录企业id
     */
    void addBatch(FarmSeaBatch farmSeaBatch, Integer nodeId);

    /**
     * 源头养殖企业确认冷冻加工企业进场请求
     * 事务：修改process_batch批号状态为已确认
     * @param processBatchId 冷冻加工批号主键id
     * @param nodeId 当前登录养殖企业id（权限校验）
     */
    void confirmProcessBatch(Integer processBatchId, Integer nodeId);

    /**
     * 查询当前养殖企业下【待确认】的冷冻加工进场申请列表
     * @param nodeId 当前登录养殖企业ID
     * @return 加工申请单集合（ProcessBatch，附带加工企业nodeName）
     */
    List<ProcessBatch> getProcessApplyList(Integer nodeId);

}
