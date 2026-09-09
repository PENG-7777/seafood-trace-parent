package com.peng.node.vo;

import com.peng.node.entity.FarmSeaBatch;
import com.peng.node.entity.FishBatch;
import com.peng.node.entity.ProcessBatch;
import com.peng.node.entity.RetaBatch;
import com.peng.node.entity.WholBatch;
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
     * 源头原始批次：可能是 FishBatch(捕捞) / FarmSeaBatch(养殖)，无则为null
     */
    private Object originBatch;
}
