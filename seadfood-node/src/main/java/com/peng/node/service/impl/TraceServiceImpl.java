package com.peng.node.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peng.node.entity.ProcessBatch;
import com.peng.node.entity.RetaBatch;
import com.peng.node.entity.WholBatch;
import com.peng.node.mapper.ProcessBatchMapper;
import com.peng.node.mapper.RetaBatchMapper;
import com.peng.node.mapper.WholBatchMapper;
import com.peng.node.service.TraceService;
import com.peng.node.vo.TraceVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

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

    /**
     * 根据溯源编号查询完整溯源链路
     * @param sourceId 32位大写UUID溯源码
     * @return TraceVO 全链路数据
     */
    @Override
    public TraceVO getTraceInfoBySourceId(String sourceId) {
        // 1.根据sourceId查询零售商批号
        QueryWrapper<RetaBatch> retaWrapper = new QueryWrapper<>();
        retaWrapper.eq("source_id", sourceId);
        RetaBatch retaBatch = retaBatchMapper.selectOne(retaWrapper);
        if (retaBatch == null) {
            throw new RuntimeException("溯源码不存在，请核对编号");
        }

        // 2.根据零售商wbId查询批发商批号
        WholBatch wholBatch = wholBatchMapper.selectById(retaBatch.getWbId());
        if (wholBatch == null) {
            throw new RuntimeException("上游批发商批次数据丢失");
        }

        // 3.根据批发商sourceBatchId查询冷冻加工批号
        ProcessBatch processBatch = processBatchMapper.selectById(wholBatch.getSourceBatchId());

        // 4.组装完整链路VO返回
        TraceVO traceVO = new TraceVO();
        traceVO.setRetaBatch(retaBatch);
        traceVO.setWholBatch(wholBatch);
        traceVO.setProcessBatch(processBatch);
        traceVO.setOriginBatch(null);
        return traceVO;
    }
}
