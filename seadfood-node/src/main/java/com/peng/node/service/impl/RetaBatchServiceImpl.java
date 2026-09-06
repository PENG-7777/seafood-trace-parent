package com.peng.node.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peng.node.entity.RetaBatch;
import com.peng.node.entity.WholBatch;
import com.peng.node.mapper.RetaBatchMapper;
import com.peng.node.mapper.WholBatchMapper;
import com.peng.node.service.RetaBatchService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 零售商批号业务实现类
 */
@Service
public class RetaBatchServiceImpl extends ServiceImpl<RetaBatchMapper, RetaBatch> implements RetaBatchService {

    /**
     * 批发商批号Mapper，新增时校验上游批发商批号合法性
     */
    @Resource
    private WholBatchMapper wholBatchMapper;

    /**
     * 查询当前登录零售商自身的批号列表
     * @param nodeId 当前登录零售商企业id
     * @return 零售商批号集合
     */
    @Override
    public List<RetaBatch> getMyBatchList(Integer nodeId) {
        QueryWrapper<RetaBatch> wrapper = new QueryWrapper<>();
        wrapper.eq("node_id", nodeId);
        return this.list(wrapper);
    }

    /**
     * 新增零售商批号
     * 业务校验：上游批发商批号必须存在并且状态=3已确认
     * @param retaBatch 零售商批号表单实体
     * @param nodeId 当前登录零售商企业id
     */
    @Override
    public void addBatch(RetaBatch retaBatch, Integer nodeId) {
        // 1.获取上游批发商批号主键
        Integer sourceWbId = retaBatch.getWbId();
        WholBatch sourceWholBatch = wholBatchMapper.selectById(sourceWbId);
        if(sourceWholBatch == null){
            throw new RuntimeException("上游批发商批号不存在");
        }
        // 2.校验上游批发商批号状态必须为3‑已确认
        if(!sourceWholBatch.getState().equals(3)){
            throw new RuntimeException("上游批发商批号尚未确认，无法创建零售商批号");
        }
        // 3.强制绑定当前登录零售商企业id，防止前端篡改
        retaBatch.setNodeId(nodeId);
        // 4.默认状态为1‑新建，溯源码与二维码等待审核通过后自动生成
        if(retaBatch.getState() == null){
            retaBatch.setState(1);
        }
        // 新增时溯源标识与二维码置空，审核确认之后赋值
        retaBatch.setSourceId(null);
        retaBatch.setSourceQr(null);
        this.save(retaBatch);
    }

    /**
     * 修改零售商批号；仅【1‑新建】状态允许更新
     * @param retaBatch 表单实体
     * @param nodeId 当前登录企业id
     */
    @Override
    public void updateBatch(RetaBatch retaBatch, Integer nodeId) {
        RetaBatch dbBatch = this.getById(retaBatch.getRbId());
        if(dbBatch == null){
            throw new RuntimeException("零售商批号不存在");
        }
        // 数据权限校验：只能操作本企业批号
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        // 仅新建状态支持修改
        if(!dbBatch.getState().equals(1)){
            throw new RuntimeException("只有新建状态批号可以修改");
        }
        // 强制回填企业id
        retaBatch.setNodeId(nodeId);
        this.updateById(retaBatch);
    }

    /**
     * 删除零售商批号；仅【1‑新建】状态允许删除
     * @param rbId 零售商批号主键
     * @param nodeId 当前登录企业id
     */
    @Override
    public void deleteBatch(Integer rbId, Integer nodeId) {
        RetaBatch dbBatch = this.getById(rbId);
        if(dbBatch == null){
            throw new RuntimeException("零售商批号不存在");
        }
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        if(!dbBatch.getState().equals(1)){
            throw new RuntimeException("只有新建状态批号可以删除");
        }
        this.removeById(rbId);
    }

    /**
     * 批号下架：修改状态为4‑已下架
     * @param rbId 零售商批号主键
     * @param nodeId 当前登录企业id
     */
    @Override
    public void offShelve(Integer rbId, Integer nodeId) {
        RetaBatch dbBatch = this.getById(rbId);
        if(dbBatch == null){
            throw new RuntimeException("零售商批号不存在");
        }
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        dbBatch.setState(4);
        this.updateById(dbBatch);
    }

    /**
     * 零售商向上游批发商发送进场确认请求，状态修改为2‑待确认
     * @param rbId 零售商批号主键
     * @param nodeId 当前登录零售商企业id
     */
    @Override
    public void sendConfirmRequest(Integer rbId, Integer nodeId) {
        RetaBatch dbBatch = this.getById(rbId);
        if(dbBatch == null){
            throw new RuntimeException("零售商批号不存在");
        }
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        // 仅新建状态可以发起确认请求
        if(!dbBatch.getState().equals(1)){
            throw new RuntimeException("只有新建批号才可以发起进场确认请求");
        }
        // 修改批号状态为待确认，等待上游批发商审核
        dbBatch.setState(2);
        this.updateById(dbBatch);
    }
}
