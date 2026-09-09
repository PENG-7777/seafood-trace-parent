package com.peng.node.vo;

import lombok.Data;

/**
 * 上游原料批号下拉返回VO
 * 冷冻加工、批发商、零售商新增/编辑页面二级下拉共用
 */
@Data
public class SourceBatchVO {
    /**
     * 上游批号主键，保存给表单sourceBatchId
     */
    private Integer sourceBatchId;

    /**
     * 业务批号编码（前端展示）
     */
    private String batchId;

    /**
     * 产品名称/品种
     */
    private String type;
}
