package com.peng.node.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * 批发商产品批号实体类
 * 对应数据库表：whol_batch 批发商产品批号表
 * 批号状态：1新建，2待确认，3已确认，4已下架
 * 业务规则：批发商上游仅允许选择【冷冻加工企业】的成品批号
 * sourceNodeType 固定赋值为3（冷冻加工企业）
 * sourceBatchId 关联 process_batch.pb_id（加工成品主键）
 */
@Data
@TableName("whol_batch")
public class WholBatch {

    /**
     * 批发商批号主键ID，自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer wbId;

    /**
     * 批发商业务产品批号
     */
    private String batchId;

    /**
     * 批发商企业编号，外键关联node_info表node_id
     */
    private Integer nodeId;

    /**
     * 上游来源类型：3冷冻加工企业
     * 后端保存时强制赋值3，前端不可自定义选择上游企业类型
     */
    private Integer sourceNodeType;

    /**
     * 上游冷冻加工成品批号ID，对应 process_batch.pb_id
     * 后端校验：该加工批号必须存在且state=2（已发布）才可创建批发商批号
     */
    private Integer sourceBatchId;

    /**
     * 海产品品种
     */
    private String type;

    /**
     * 海产品质检报告编号
     */
    private String reportId;

    /**
     * 检验人员名称
     */
    private String testName;

    /**
     * 批号录入日期，数据库date类型
     */
    private LocalDate batchDate;

    /**
     * 批号状态:1新建,2待确认,3已确认,4已下架，数据库默认值1
     */
    private Integer state;

    /**
     * 备注
     */
    private String remarks;

}
