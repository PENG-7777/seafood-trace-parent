package com.peng.node.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peng.node.entity.FarmSeaBatch;
import com.peng.node.entity.FishBatch;
import com.peng.node.entity.NodeInfo;
import com.peng.node.entity.ProcessBatch;
import com.peng.node.entity.RetaBatch;
import com.peng.node.entity.WholBatch;
import com.peng.node.mapper.FarmSeaBatchMapper;
import com.peng.node.mapper.FishBatchMapper;
import com.peng.node.mapper.NodeInfoMapper;
import com.peng.node.mapper.ProcessBatchMapper;
import com.peng.node.mapper.RetaBatchMapper;
import com.peng.node.mapper.WholBatchMapper;
import com.peng.node.service.TraceService;
import com.peng.node.vo.TraceVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 溯源查询业务实现类
 */
@Service
public class TraceServiceImpl implements TraceService {

    @Resource
    private RetaBatchMapper retaBatchMapper;

    @Resource
    private WholBatchMapper wholBatchMapper;

    @Resource
    private ProcessBatchMapper processBatchMapper;

    @Resource
    private FishBatchMapper fishBatchMapper;

    @Resource
    private FarmSeaBatchMapper farmSeaBatchMapper;

    @Resource
    private NodeInfoMapper nodeInfoMapper;

    /**
     * 根据溯源编号查询完整溯源链路
     * @param sourceId 溯源码
     * @return TraceVO 全链路数据
     */
    @Override
    public TraceVO getTraceInfoBySourceId(String sourceId) {
        TraceVO traceVO = new TraceVO();

        // 1. 查询零售批次（溯源码绑定在零售环节）
        QueryWrapper<RetaBatch> retaWrapper = new QueryWrapper<>();
        retaWrapper.eq("source_id", sourceId);
        RetaBatch retaBatch = retaBatchMapper.selectOne(retaWrapper);

        if (retaBatch == null) {
            throw new RuntimeException("溯源码不存在，请核对编号");
        }
        // 查询零售企业名称
        NodeInfo retaNode = nodeInfoMapper.selectById(retaBatch.getNodeId());
        if (retaNode != null) {
            retaBatch.setNodeName(retaNode.getName());
        }
        traceVO.setRetaBatch(retaBatch);

        // 2. 通过零售的wbId查询批发商批次
        WholBatch wholBatch = wholBatchMapper.selectById(retaBatch.getWbId());
        if (wholBatch == null) {
            throw new RuntimeException("上游批发商批次数据丢失");
        }
        // 查询批发企业名称
        NodeInfo wholNode = nodeInfoMapper.selectById(wholBatch.getNodeId());
        if (wholNode != null) {
            wholBatch.setNodeName(wholNode.getName());
        }
        traceVO.setWholBatch(wholBatch);

        // 3. 通过批发商的sourceBatchId查询加工批次
        ProcessBatch processBatch = processBatchMapper.selectById(wholBatch.getSourceBatchId());
        if (processBatch == null) {
            throw new RuntimeException("上游加工批次数据丢失");
        }
        // 查询加工企业名称
        NodeInfo processNode = nodeInfoMapper.selectById(processBatch.getNodeId());
        if (processNode != null) {
            processBatch.setNodeName(processNode.getName());
        }
        traceVO.setProcessBatch(processBatch);

        // 4. 查询源头批次：1捕捞，2养殖
        Integer sourceNodeType = processBatch.getSourceNodeType();
        Integer sourceBatchId = processBatch.getSourceBatchId();
        Object originBatch = null;
        if (sourceNodeType != null && sourceBatchId != null) {
            if (sourceNodeType == 1) {
                // 捕捞企业源头
                FishBatch fishBatch = fishBatchMapper.selectById(sourceBatchId);
                if (fishBatch != null) {
                    NodeInfo fishNode = nodeInfoMapper.selectById(fishBatch.getNodeId());
                    if (fishNode != null) {
                        fishBatch.setNodeName(fishNode.getName());
                    }
                    originBatch = fishBatch;
                }
            } else if (sourceNodeType == 2) {
                // 养殖企业源头
                FarmSeaBatch farmSeaBatch = farmSeaBatchMapper.selectById(sourceBatchId);
                if (farmSeaBatch != null) {
                    NodeInfo farmNode = nodeInfoMapper.selectById(farmSeaBatch.getNodeId());
                    if (farmNode != null) {
                        farmSeaBatch.setNodeName(farmNode.getName());
                    }
                    originBatch = farmSeaBatch;
                }
            }
        }
        traceVO.setOriginBatch(originBatch);

        return traceVO;
    }
}
