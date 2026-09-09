package com.peng.node.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 零售商进场申请VO
 * 用于批发商确认页面 confirmList.vue 展示申请卡片数据
 */
@Data
public class RetailApplyVO {
    /**
     * 零售商批号主键 rbId
     */
    private Integer retaBatchId;
    /**
     * 批发商批号主键 wbId
     */
    private Integer wholBatchId;
    /**
     * 零售商企业名称
     */
    private String retaNodeName;
    /**
     * 申请提交时间（reta_batch更新state=2的时间）
     */
    private LocalDateTime applyTime;
    /**
     * 申请状态：1待批发商确认，2已确认
     */
    private Integer applyState;
}
