package com.peng.node.vo;

import java.time.LocalDate;

public class ProcessApplyVO {
    //加工批号主键
    private Integer processBatchId;
    //上游捕捞批号编号
    private String fishBatchId;
    //下游加工企业名称
    private String processNodeName;
    //加工完成日期（替代不存在的apply_time）
    private LocalDate applyTime;
    //批号状态：1待发布，2已发布，3已下架（替代apply_state）
    private Integer applyState;

    public Integer getProcessBatchId() {
        return processBatchId;
    }

    public void setProcessBatchId(Integer processBatchId) {
        this.processBatchId = processBatchId;
    }

    public String getFishBatchId() {
        return fishBatchId;
    }

    public void setFishBatchId(String fishBatchId) {
        this.fishBatchId = fishBatchId;
    }

    public String getProcessNodeName() {
        return processNodeName;
    }

    public void setProcessNodeName(String processNodeName) {
        this.processNodeName = processNodeName;
    }

    public LocalDate getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(LocalDate applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getApplyState() {
        return applyState;
    }

    public void setApplyState(Integer applyState) {
        this.applyState = applyState;
    }
}
