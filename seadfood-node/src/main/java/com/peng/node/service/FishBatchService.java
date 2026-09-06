package com.peng.node.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.peng.node.entity.FishBatch;
import java.util.List;

/**
 * 捕捞企业批号业务接口
 */
public interface FishBatchService extends IService<FishBatch> {

    /**
     * 查询当前捕捞企业自己的批号列表
     * @param nodeId 当前登录捕捞企业id
     * @return 批号集合
     */
    List<FishBatch> getMyBatchList(Integer nodeId);

    /**
     * 下架批号：修改状态为3‑已下架
     * @param fbId 捕捞批号主键
     * @param nodeId 当前登录企业id（数据权限校验）
     */
    void offShelve(Integer fbId, Integer nodeId);

    /**
     * 删除批号；仅【待发布】状态允许删除
     * @param fbId 捕捞批号主键
     * @param nodeId 当前登录企业id（数据权限校验）
     */
    void deleteBatch(Integer fbId, Integer nodeId);

    /**
     * 更新捕捞批号；仅【待发布】状态允许更新
     * @param fishBatch 批号表单数据
     * @param nodeId 当前登录企业id
     */
    void updateBatch(FishBatch fishBatch, Integer nodeId);

    /**
     * 新增捕捞企业批号
     * @param fishBatch 批号实体
     * @param nodeId 当前登录企业id
     */
    void addBatch(FishBatch fishBatch, Integer nodeId);

    /**
     * 源头捕捞企业确认冷冻加工企业进场请求
     * 事务：修改process_batch批号状态为已确认
     * @param processBatchId 冷冻加工批号主键id
     * @param nodeId 当前登录捕捞企业id（权限校验）
     */
    void confirmProcessBatch(Integer processBatchId, Integer nodeId);
}
