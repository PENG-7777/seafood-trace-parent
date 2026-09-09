package com.peng.node.vo;

import lombok.Data;

@Data
public class WholApplyVO {
    /** 批发商批号主键 whol_batch_id */
    private Integer wholBatchId;
    /** 上游冷冻加工批号主键 source_batch_id */
    private Integer processBatchId;
    /** 下游批发企业名称 */
    private String wholNodeName;
    /** 申请时间 */
    private String applyTime;
    /** 申请状态：2=待加工企业确认，3=已确认 */
    private Integer applyState;
}
