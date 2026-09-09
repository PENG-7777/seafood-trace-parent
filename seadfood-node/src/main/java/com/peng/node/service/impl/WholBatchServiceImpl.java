package com.peng.node.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peng.node.entity.ProcessBatch;
import com.peng.node.entity.RetaBatch;
import com.peng.node.entity.WholBatch;
import com.peng.node.mapper.ProcessBatchMapper;
import com.peng.node.mapper.RetaBatchMapper;
import com.peng.node.mapper.WholBatchMapper;
import com.peng.node.service.WholBatchService;
import com.peng.node.util.TraceIdUtil;
import com.peng.node.vo.RetailApplyVO;
import com.peng.node.vo.SourceBatchVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 批发商批号业务实现类
 * 完整业务链路：海水养殖/捕捞 → 冷冻加工企业(process_batch) → 批发商(whol_batch) → 零售商(reta_batch)
 * 上游数据源：process_batch（冷冻加工成品批号）
 * 下游数据源：reta_batch（零售商销售批号）
 */
@Service
public class WholBatchServiceImpl extends ServiceImpl<WholBatchMapper, WholBatch> implements WholBatchService {

    /**
     * 冷冻加工批号Mapper，新增批发商批号时校验上游加工批号合法性
     */
    @Resource
    private ProcessBatchMapper processBatchMapper;

    /**
     * 零售商批号Mapper，用于审核零售商进场请求、修改零售商批号状态与溯源码
     */
    @Resource
    private RetaBatchMapper retaBatchMapper;

    /**
     * 查询当前批发商企业自己的批号列表
     * @param nodeId 当前登录批发商企业id
     * @return 批发商批号集合
     */
    @Override
    public List<WholBatch> getMyBatchList(Integer nodeId) {
        QueryWrapper<WholBatch> wrapper = new QueryWrapper<>();
        wrapper.eq("node_id", nodeId);
        return this.list(wrapper);
    }

    /**
     * 新增批发商批号
     * 业务校验：上游冷冻加工批号必须存在并且状态=3已确认；sourceNodeType后端强制赋值3（冷冻加工企业类型）
     * @param wholBatch 批发商批号表单实体
     * @param nodeId 当前登录批发商企业id
     */
    @Override
    public void addBatch(WholBatch wholBatch, Integer nodeId) {
        //1.获取前端传入的上游冷冻加工批号主键pbId
        Integer sourcePbId = wholBatch.getSourceBatchId();
        ProcessBatch sourceProcessBatch = processBatchMapper.selectById(sourcePbId);
        if(sourceProcessBatch == null){
            throw new RuntimeException("上游冷冻加工批号不存在");
        }
        //2.上游加工批号必须是【3‑已确认】才允许被批发商引用作为原料
        if(!sourceProcessBatch.getState().equals(3)){
            throw new RuntimeException("上游冷冻加工批号未确认，不能创建批发商批号");
        }
        //3.后端强制设置上游来源类型=3（冷冻加工企业），屏蔽前端传入的值保证数据规范
        wholBatch.setSourceNodeType(3);
        //4.强制设置所属批发商企业id，防止前端篡改实现数据权限隔离
        wholBatch.setNodeId(nodeId);
        //5.默认批号状态：1‑新建
        if(wholBatch.getState() == null){
            wholBatch.setState(1);
        }
        this.save(wholBatch);
    }

    /**
     * 更新批发商批号；仅【1‑新建】状态允许更新
     * @param wholBatch 表单实体
     * @param nodeId 当前登录企业id
     */
    @Override
    public void updateBatch(WholBatch wholBatch, Integer nodeId) {
        WholBatch dbBatch = this.getById(wholBatch.getWbId());
        if(dbBatch == null){
            throw new RuntimeException("批发商批号不存在");
        }
        //数据权限校验：只能操作本企业名下的批号
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        //业务规则：只有新建状态允许修改批号信息
        if(!dbBatch.getState().equals(1)){
            throw new RuntimeException("只有新建状态批号可以修改");
        }
        //更新时强制锁定上游来源类型为3（冷冻加工）
        wholBatch.setSourceNodeType(3);
        wholBatch.setNodeId(nodeId);
        this.updateById(wholBatch);
    }

    /**
     * 删除批号；仅【1‑新建】状态允许删除
     * @param wbId 批发商批号主键
     * @param nodeId 当前登录企业id
     */
    @Override
    public void deleteBatch(Integer wbId, Integer nodeId) {
        WholBatch dbBatch = this.getById(wbId);
        if(dbBatch == null){
            throw new RuntimeException("批发商批号不存在");
        }
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        if(!dbBatch.getState().equals(1)){
            throw new RuntimeException("只有新建状态批号可以删除");
        }
        this.removeById(wbId);
    }

    /**
     * 批号下架：修改状态为4‑已下架
     * @param wbId 批发商批号主键
     * @param nodeId 当前登录企业id
     */
    @Override
    public void offShelve(Integer wbId, Integer nodeId) {
        WholBatch dbBatch = this.getById(wbId);
        if(dbBatch == null){
            throw new RuntimeException("批发商批号不存在");
        }
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        dbBatch.setState(4);
        this.updateById(dbBatch);
    }

    /**
     * 向上游冷冻加工企业发送进场确认请求，状态修改为2‑待确认
     * @param wbId 批发商批号主键
     * @param nodeId 当前登录批发商企业id
     */
    @Override
    public void sendConfirmRequest(Integer wbId, Integer nodeId) {
        WholBatch dbBatch = this.getById(wbId);
        if(dbBatch == null){
            throw new RuntimeException("批发商批号不存在");
        }
        if(!dbBatch.getNodeId().equals(nodeId)){
            throw new RuntimeException("无权操作该批号");
        }
        //业务约束：只有新建状态才可以向上游发起进场确认请求
        if(!dbBatch.getState().equals(1)){
            throw new RuntimeException("只有新建批号才可以发起进场确认请求");
        }
        //修改批号状态为待确认，等待上游冷冻加工企业审核确认
        dbBatch.setState(2);
        this.updateById(dbBatch);
    }

    /**
     * 批发商确认零售商进场请求
     * 事务：更新reta_batch零售商批号状态为已确认；状态变更为已确认自动生成溯源标识码sourceId与溯源二维码
     * @param retaBatchId 零售商批号主键
     * @param nodeId 当前登录批发商企业id（权限校验）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmRetaBatch(Integer retaBatchId, Integer nodeId) {
        // 1.查询零售商批号记录
        RetaBatch retaBatch = retaBatchMapper.selectById(retaBatchId);
        if (retaBatch == null) {
            throw new RuntimeException("零售商批号不存在");
        }

        // 2.拿到零售商绑定的上游批发商主键wbId
        Integer wholBatchId = retaBatch.getWbId();
        WholBatch sourceWholBatch = this.getById(wholBatchId);
        if (sourceWholBatch == null) {
            throw new RuntimeException("上游批发商原料批号不存在");
        }

        // 3.权限校验：上游批发商必须属于当前登录操作的批发商企业
        if (!sourceWholBatch.getNodeId().equals(nodeId)) {
            throw new RuntimeException("无权确认非本企业原料对应的零售商批号");
        }

        // 4.校验上游批发商批号必须已经确认完成 state=3
        if (!sourceWholBatch.getState().equals(3)) {
            throw new RuntimeException("上游批发商批号尚未确认，无法审核零售商进场");
        }

        // 5.零售商批号必须处于【2‑待确认】状态才可审核通过
        if (!retaBatch.getState().equals(2)) {
            throw new RuntimeException("仅待确认状态的零售商批号可执行审核操作");
        }

        // 6.调用工具类生成全局唯一溯源编码与二维码Base64字符串
        String sourceId = TraceIdUtil.generateSourceId();
        String qrBase64 = TraceIdUtil.generateQrCodeBase64(sourceId);

        // 7.修改零售商批号状态为3‑已确认，写入溯源标识与二维码
        retaBatch.setState(3);
        retaBatch.setSourceId(sourceId);
        retaBatch.setSourceQr(qrBase64);

        // 8.保存修改数据
        retaBatchMapper.updateById(retaBatch);
    }

    /**
     * 根据上游冷冻加工企业ID，下拉查询该企业【state=3已确认】的加工成品批号
     * 用于新增批发商页面二级联动下拉选择上游原料
     * @param sourceNodeId 上游冷冻加工企业编号
     * @return 上游原料下拉VO列表
     */
    @Override
    public List<SourceBatchVO> getUpstreamProcessBatchByNodeId(Integer sourceNodeId) {
        QueryWrapper<ProcessBatch> queryWrapper = new QueryWrapper<>();
        // 查询指定冷冻加工企业，并且状态=3已确认的成品批号
        queryWrapper.eq("node_id", sourceNodeId);
        queryWrapper.eq("state", 3);
        List<ProcessBatch> processBatchList = processBatchMapper.selectList(queryWrapper);

        List<SourceBatchVO> voList = new ArrayList<>();
        for (ProcessBatch batch : processBatchList) {
            SourceBatchVO vo = new SourceBatchVO();
            vo.setSourceBatchId(batch.getPbId());
            vo.setBatchId(batch.getBatchId());
            vo.setType(batch.getType());
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 查询提交给当前登录批发商的所有零售商进场申请列表
     * @param nodeId 当前登录批发商企业ID
     * @return 零售商申请VO集合
     */
    @Override
    public List<RetailApplyVO> getRetaApplyList(Integer nodeId) {
        // 业务逻辑：
        // 1.查询当前批发商名下全部whol_batch
        // 2.关联查询reta_batch中 wb_id属于这批wholBatch、state=2/3 的记录
        // 3.关联node_info拿到零售商企业名称
        // 使用自定义Mapper关联查询，编写XML多表联查
        return retaBatchMapper.selectRetaApplyListByWholNodeId(nodeId);
    }

}
