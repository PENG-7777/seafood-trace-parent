package com.peng.node.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peng.node.entity.ProcessBatch;
import com.peng.node.entity.WholBatch;
import com.peng.node.mapper.ProcessBatchMapper;
import com.peng.node.mapper.WholBatchMapper;
import com.peng.node.service.ProcessBatchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 冷冻加工企业批号业务实现类
 */
@Service
public class ProcessBatchServiceImpl extends ServiceImpl<ProcessBatchMapper, ProcessBatch> implements ProcessBatchService {

    /**
     * 批发商批号Mapper，用于查询、更新批发商批号状态
     */
    @Resource
    private WholBatchMapper wholBatchMapper;

    /**
     * 查询当前冷冻加工企业自己的批号列表
     * @param nodeId 当前登录冷冻加工企业id
     * @return 加工批号集合
     */
    @Override
    public List<ProcessBatch> getMyBatchList(Integer nodeId) {
        QueryWrapper<ProcessBatch> wrapper = new QueryWrapper<>();
        wrapper.eq("node_id", nodeId);
        return this.list(wrapper);
    }

    /**
     * 新增冷冻加工批号
     * @param processBatch 加工批号表单实体
     * @param nodeId 当前登录企业id
     */
    @Override
    public void addBatch(ProcessBatch processBatch, Integer nodeId) {
        // 强制设置所属加工企业id，防止前端篡改
        processBatch.setNodeId(nodeId);
        // 默认状态：1‑待发布
        if(processBatch.getState() == null){
            processBatch.setState(1);
        }
        this.save(processBatch);
    }

    /**
     * 更新冷冻加工批号；仅【待发布】状态(state=1)允许更新
     * @param processBatch 表单实体
     * @param nodeId 当前登录企业id
     */
    @Override
    public void updateBatch(ProcessBatch processBatch, Integer nodeId) {
        ProcessBatch dbBatch = this.getById(processBatch.getPbId());
        if(dbBatch == null){
            throw new RuntimeException("加工批号不存在");
        }
        // 数据权限校验：只能操作本企业批号
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        // 只有待发布允许修改
        if(!dbBatch.getState().equals(1)){
            throw new RuntimeException("只有待发布状态批号可以修改");
        }
        processBatch.setNodeId(nodeId);
        this.updateById(processBatch);
    }

    /**
     * 删除批号；仅【待发布】状态允许删除
     * @param pbId 加工批号主键
     * @param nodeId 当前登录企业id
     */
    @Override
    public void deleteBatch(Integer pbId, Integer nodeId) {
        ProcessBatch dbBatch = this.getById(pbId);
        if(dbBatch == null){
            throw new RuntimeException("加工批号不存在");
        }
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        if(!dbBatch.getState().equals(1)){
            throw new RuntimeException("只有待发布状态批号可以删除");
        }
        this.removeById(pbId);
    }

    /**
     * 批号下架：修改状态为4‑已下架
     * @param pbId 加工批号主键
     * @param nodeId 当前登录企业id
     */
    @Override
    public void offShelve(Integer pbId, Integer nodeId) {
        ProcessBatch dbBatch = this.getById(pbId);
        if(dbBatch == null){
            throw new RuntimeException("加工批号不存在");
        }
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        dbBatch.setState(4);
        this.updateById(dbBatch);
    }

    /**
     * 向上游源头（捕捞/养殖）发送进场确认请求，状态修改为2‑待确认
     * @param pbId 加工批号主键
     * @param nodeId 当前登录冷冻加工企业id
     */
    @Override
    public void sendConfirmRequest(Integer pbId, Integer nodeId) {
        ProcessBatch dbBatch = this.getById(pbId);
        if(dbBatch == null){
            throw new RuntimeException("加工批号不存在");
        }
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        // 只有待发布才可以向上游发起确认请求
        if(!dbBatch.getState().equals(1)){
            throw new RuntimeException("只有待发布批号才可以发起进场确认请求");
        }
        // 修改状态为待确认，等待上游捕捞/养殖企业确认
        dbBatch.setState(2);
        this.updateById(dbBatch);
    }

    /**
     * 冷冻加工企业确认批发商进场请求
     * 事务：更新whol_batch批发商批号状态为已确认
     * @param wholBatchId 批发商批号主键
     * @param nodeId 当前登录冷冻加工企业id（权限校验）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmWholBatch(Integer wholBatchId, Integer nodeId) {
        // 1. 查询批发商批号记录
        WholBatch wholBatch = wholBatchMapper.selectById(wholBatchId);
        if (wholBatch == null) {
            throw new RuntimeException("批发商批号不存在");
        }

        // 2. 获取批发商批号关联的上游冷冻加工批号主键
        Integer sourceProcessPbId = wholBatch.getSourceBatchId();
        ProcessBatch sourceProcessBatch = this.getById(sourceProcessPbId);
        if (sourceProcessBatch == null) {
            throw new RuntimeException("上游冷冻加工原料批号不存在");
        }

        // 3. 权限校验：该上游加工批号必须属于当前登录冷冻加工企业
        if (!sourceProcessBatch.getNodeId().equals(nodeId)) {
            throw new RuntimeException("无权确认非本企业原料的批发商批号");
        }

        // 4. 校验上游加工批号必须为【3‑已确认】，原料本身必须已经经过源头确认
        if (!sourceProcessBatch.getState().equals(3)) {
            throw new RuntimeException("上游冷冻加工原料批号尚未确认，无法确认批发商进场");
        }

        // 5. 批发商批号必须处于【2‑待确认】状态，才允许确认
        if (!wholBatch.getState().equals(2)) {
            throw new RuntimeException("仅可确认状态为待确认的批发商批号");
        }

        // 6. 修改批发商批号状态为3‑已确认，可以继续向下游零售商流转
        wholBatch.setState(3);
        wholBatchMapper.updateById(wholBatch);
    }
}
