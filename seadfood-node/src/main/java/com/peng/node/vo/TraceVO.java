package com.peng.node.vo;

import com.peng.node.entity.*;
import lombok.Data;

/**
 * 溯源全链路返回VO
 */
@Data
public class TraceVO {
    /**
     * 零售商批号信息（末端）
     */
    private RetaBatch retaBatch;
    /**
     * 批发商批号信息
     */
    private WholBatch wholBatch;
    /**
     * 冷冻加工批号信息
     */
    private ProcessBatch processBatch;
    /**
     * 养殖/捕捞原始批次（如果后续扩展上游源表）
     */
    private Object originBatch;
}
