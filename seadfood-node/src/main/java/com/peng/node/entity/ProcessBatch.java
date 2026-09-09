package com.peng.node.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * 冷冻加工企业成品批号实体
 * 对应数据库表：process_batch
 * 批号状态：1待发布，2已发布，3已下架
 * 上游原料来源类型：1捕捞企业，2养殖企业
 */
@Data
@TableName("process_batch")
public class ProcessBatch {

    /**
     * 加工批号主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer pbId;

    /**
     * 加工企业成品业务批号
     */
    private String batchId;

    /**
     * 所属冷冻加工企业编号，关联node_info.node_id
     */
    private Integer nodeId;

    /**
     * 上游原料来源：1捕捞企业，2养殖企业
     */
    private Integer sourceNodeType;

    /**
     * 上游原料批号ID
     * sourceNodeType=1 对应 fish_batch.fb_id
     * sourceNodeType=2 对应 farm_sea_batch.fsb_id
     */
    private Integer sourceBatchId;

    /**
     * 冷冻海产品成品品种
     */
    private String type;

    /**
     * 加工生产流水编号
     */
    private String processNo;

    /**
     * 加工操作人员
     */
    private String processPerson;

    /**
     * 冷冻加工完成日期
     */
    private LocalDate processDate;

    /**
     * 冷冻仓储存放信息
     */
    private String coldStorageInfo;

    /**
     * 批号状态：1待发布，2已发布，3已下架，数据库默认值1
     */
    private Integer state;

    /**
     * 备注
     */
    private String remarks;

    @TableField(exist = false)
    private String nodeName;

}
