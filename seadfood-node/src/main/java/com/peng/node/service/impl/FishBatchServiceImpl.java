package com.peng.node.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peng.node.entity.FishBatch;
import com.peng.node.entity.NodeInfo;
import com.peng.node.entity.ProcessBatch;
import com.peng.node.mapper.FishBatchMapper;
import com.peng.node.mapper.NodeInfoMapper;
import com.peng.node.mapper.ProcessBatchMapper;
import com.peng.node.service.FishBatchService;
import com.peng.node.vo.ProcessApplyVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 捕捞企业批号业务实现类
 */
@Service
public class FishBatchServiceImpl extends ServiceImpl<FishBatchMapper, FishBatch> implements FishBatchService {

    /**
     * 冷冻加工批号Mapper，用于查询、更新冷冻加工批号状态
     */
    @Resource
    private ProcessBatchMapper processBatchMapper;

    /**
     * 节点企业Mapper，查询捕捞企业信息
     */
    @Resource
    private NodeInfoMapper nodeInfoMapper;

    /**
     * 查询当前捕捞企业自己的批号列表
     * @param nodeId 当前登录捕捞企业id
     * @return 批号集合
     */
    @Override
    public List<FishBatch> getMyBatchList(Integer nodeId) {
        QueryWrapper<FishBatch> wrapper = new QueryWrapper<>();
        wrapper.eq("node_id", nodeId);
        return this.list(wrapper);
    }

    /**
     * 下架批号：修改状态为3‑已下架
     * @param fbId 捕捞批号主键
     * @param nodeId 当前登录企业id（数据权限校验）
     */
    @Override
    public void offShelve(Integer fbId, Integer nodeId) {
        FishBatch fishBatch = this.getById(fbId);
        if(fishBatch == null){
            throw new RuntimeException("批号不存在");
        }
        // 数据权限：只能操作自己企业的批号
        if(!fishBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        // 设置状态：3已下架
        fishBatch.setState(3);
        this.updateById(fishBatch);
    }

    /**
     * 删除批号；仅【待发布】状态允许删除 state=1
     * @param fbId 捕捞批号主键
     * @param nodeId 当前登录企业id（数据权限校验）
     */
    @Override
    public void deleteBatch(Integer fbId, Integer nodeId) {
        FishBatch fishBatch = this.getById(fbId);
        if(fishBatch == null){
            throw new RuntimeException("批号不存在");
        }
        if(!fishBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        // 只有待发布才允许删除
        if(!fishBatch.getState().equals(1)){
            throw new RuntimeException("只有待发布状态批号可以删除");
        }
        this.removeById(fbId);
    }

    /**
     * 更新捕捞批号；仅【待发布】状态允许更新
     * @param fishBatch 批号表单数据
     * @param nodeId 当前登录企业id
     */
    @Override
    public void updateBatch(FishBatch fishBatch, Integer nodeId) {
        FishBatch dbBatch = this.getById(fishBatch.getFbId());
        if(dbBatch == null){
            throw new RuntimeException("批号不存在");
        }
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        if(!dbBatch.getState().equals(1)){
            throw new RuntimeException("只有待发布状态批号可以修改");
        }
        // 回填企业id，防止前端篡改
        fishBatch.setNodeId(nodeId);
        this.updateById(fishBatch);
    }

    /**
     * 新增捕捞企业批号
     * @param fishBatch 批号实体
     * @param nodeId 当前登录企业id
     */
    @Override
    public void addBatch(FishBatch fishBatch, Integer nodeId) {
        // 强制设置所属企业id，防止前端篡改
        fishBatch.setNodeId(nodeId);
        // 默认状态待发布1，如果前端不传
        if(fishBatch.getState() == null){
            fishBatch.setState(1);
        }
        this.save(fishBatch);
    }

    /**
     * 源头捕捞企业确认冷冻加工企业进场请求
     * 事务：修改process_batch批号状态为已确认
     * @param processBatchId 冷冻加工批号主键id
     * @param nodeId 当前登录捕捞企业id（权限校验）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmProcessBatch(Integer processBatchId, Integer nodeId) {
        // 1. 根据主键查询冷冻加工批号记录
        ProcessBatch processBatch = processBatchMapper.selectById(processBatchId);
        if(processBatch == null){
            throw new RuntimeException("冷冻加工批号不存在");
        }

        // 2. 校验上游来源类型，必须为1：捕捞企业
        if(!processBatch.getSourceNodeType().equals(1)){
            throw new RuntimeException("该加工批号上游原料不是捕捞企业，不能由捕捞企业确认");
        }

        // 3. 获取上游捕捞批号主键，查询源头捕捞批号
        Integer sourceFbId = processBatch.getSourceBatchId();
        FishBatch sourceFishBatch = this.getById(sourceFbId);
        if(sourceFishBatch == null){
            throw new RuntimeException("上游捕捞源头批号不存在");
        }

        // 4. 数据权限校验：上游捕捞批号必须属于当前登录捕捞企业
        if(!sourceFishBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权确认非本企业原料的冷冻加工批号");
        }

        // 5. 校验冷冻加工批号状态，只能确认【2‑待确认】状态
        if(!processBatch.getState().equals(2)){
            throw new RuntimeException("仅可确认状态为待确认的加工批号");
        }

        // 6. 修改冷冻加工批号状态为3‑已确认，允许向下游批发商流转
        processBatch.setState(3);
        processBatchMapper.updateById(processBatch);
    }

    @Override
    public List<ProcessApplyVO> getProcessApplyList(Integer nodeId) {
        //根据当前捕捞企业nodeId，查询所有待确认/已确认的加工进场申请
        return baseMapper.selectApplyListByNodeId(nodeId);
    }



}
