package com.peng.node.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peng.node.entity.FarmSeaBatch;
import com.peng.node.entity.ProcessBatch;
import com.peng.node.mapper.FarmSeaBatchMapper;
import com.peng.node.mapper.ProcessBatchMapper;
import com.peng.node.service.FarmSeaBatchService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 海水养殖企业批号业务实现类
 */
@Service
public class FarmSeaBatchServiceImpl extends ServiceImpl<FarmSeaBatchMapper, FarmSeaBatch> implements FarmSeaBatchService {

    /**
     * 冷冻加工批号Mapper，用于查询、更新加工批号状态
     */
    @Resource
    private ProcessBatchMapper processBatchMapper;

    /**
     * 查询当前养殖企业自己的批号列表
     * @param nodeId 当前登录养殖企业id
     * @return 批号集合
     */
    @Override
    public List<FarmSeaBatch> getMyBatchList(Integer nodeId) {
        QueryWrapper<FarmSeaBatch> wrapper = new QueryWrapper<>();
        wrapper.eq("node_id", nodeId);
        return this.list(wrapper);
    }

    /**
     * 批号下架：修改状态为3‑已下架
     * @param fsbId 养殖批号主键
     * @param nodeId 当前登录企业id（数据权限校验）
     */
    @Override
    public void offShelve(Integer fsbId, Integer nodeId) {
        FarmSeaBatch farmSeaBatch = this.getById(fsbId);
        if(farmSeaBatch == null){
            throw new RuntimeException("批号不存在");
        }
        if(!farmSeaBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        farmSeaBatch.setState(3);
        this.updateById(farmSeaBatch);
    }

    /**
     * 删除批号；仅【待发布】状态(state=1)允许删除
     * @param fsbId 养殖批号主键
     * @param nodeId 当前登录企业id（数据权限校验）
     */
    @Override
    public void deleteBatch(Integer fsbId, Integer nodeId) {
        FarmSeaBatch farmSeaBatch = this.getById(fsbId);
        if(farmSeaBatch == null){
            throw new RuntimeException("批号不存在");
        }
        if(!farmSeaBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        if(!farmSeaBatch.getState().equals(1)){
            throw new RuntimeException("只有待发布状态批号可以删除");
        }
        this.removeById(fsbId);
    }

    /**
     * 更新养殖批号；仅【待发布】状态允许更新
     * @param farmSeaBatch 批号表单数据
     * @param nodeId 当前登录企业id
     */
    @Override
    public void updateBatch(FarmSeaBatch farmSeaBatch, Integer nodeId) {
        FarmSeaBatch dbBatch = this.getById(farmSeaBatch.getFsbId());
        if(dbBatch == null){
            throw new RuntimeException("批号不存在");
        }
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        if(!dbBatch.getState().equals(1)){
            throw new RuntimeException("只有待发布状态批号可以修改");
        }
        farmSeaBatch.setNodeId(nodeId);
        this.updateById(farmSeaBatch);
    }

    /**
     * 新增海水养殖企业批号
     * @param farmSeaBatch 批号实体
     * @param nodeId 当前登录企业id
     */
    @Override
    public void addBatch(FarmSeaBatch farmSeaBatch, Integer nodeId) {
        farmSeaBatch.setNodeId(nodeId);
        if(farmSeaBatch.getState() == null){
            farmSeaBatch.setState(1);
        }
        this.save(farmSeaBatch);
    }

    /**
     * 源头养殖企业确认冷冻加工企业进场请求
     * 事务：修改process_batch批号状态为已确认
     * @param processBatchId 冷冻加工批号主键id
     * @param nodeId 当前登录养殖企业id（权限校验）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmProcessBatch(Integer processBatchId, Integer nodeId) {
        //1. 查询冷冻加工批号
        ProcessBatch processBatch = processBatchMapper.selectById(processBatchId);
        if(processBatch == null){
            throw new RuntimeException("冷冻加工批号不存在");
        }

        //2. 校验上游来源类型必须为2=海水养殖企业
        if(!processBatch.getSourceNodeType().equals(2)){
            throw new RuntimeException("该加工批号上游不是养殖企业，无法由养殖企业确认");
        }

        //3. 根据加工批号中sourceBatchId拿到对应的养殖源头批号
        Integer sourceFsbId = processBatch.getSourceBatchId();
        FarmSeaBatch sourceFarmBatch = this.getById(sourceFsbId);
        if(sourceFarmBatch == null){
            throw new RuntimeException("上游养殖源头批号不存在");
        }

        //4. 权限校验：上游养殖批号必须属于当前登录的养殖企业
        if(!sourceFarmBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权确认非本企业原料的加工批号");
        }

        //5. 校验加工批号状态必须为【2‑待确认】才允许确认
        if(!processBatch.getState().equals(2)){
            throw new RuntimeException("只能确认状态为待确认的加工批号");
        }

        //6. 修改冷冻加工批号状态为 3‑已确认，加工批号可以向下游流转
        processBatch.setState(3);
        processBatchMapper.updateById(processBatch);
    }

    /**
     * 查询当前养殖企业下【待确认】的冷冻加工进场申请列表
     * 查询逻辑：
     * 1.先查出当前企业所有养殖批号ID集合
     * 2.关联查询加工表+node_info表，sourceBatchId属于这批批号 并且 state=2（待确认）的数据
     * @param nodeId 当前登录养殖企业ID
     * @return 待审核加工申请单（携带加工企业nodeName）
     */
    @Override
    public List<ProcessBatch> getProcessApplyList(Integer nodeId) {
        //1. 获取当前养殖企业全部养殖批号
        QueryWrapper<FarmSeaBatch> farmWrapper = new QueryWrapper<>();
        farmWrapper.eq("node_id", nodeId);
        List<FarmSeaBatch> farmList = this.list(farmWrapper);
        if(farmList.isEmpty()){
            return List.of();
        }
        //收集本企业所有养殖批号主键
        List<Integer> fsbIdList = farmList.stream()
                .map(FarmSeaBatch::getFsbId)
                .toList();

        //2. 调用mapper自定义方法，关联node_info查询加工企业名称
        return processBatchMapper.selectProcessApplyList(fsbIdList);
    }

}
