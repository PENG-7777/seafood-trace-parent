package com.peng.node.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * 零售商产品批号实体类
 * 对应数据库表：reta_batch 零售商产品批号表（末端，生成溯源标识码）
 * 批号状态：1新建,2待确认,3已确认,4已下架
 * 业务规则：
 * 1. 上游只能选择状态为【3已确认】的批发商批号（whol_batch）
 * 2. 批发商审核确认后，当前批号state更新为3（已确认）
 * 3. state=3已确认时，后端自动生成唯一sourceId溯源标识码，生成sourceQr二维码Base64字符串
 */
@Data
@TableName("reta_batch")
public class RetaBatch {

    /**
     * 零售商批号主键ID，自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer rbId;

    /**
     * 零售商业务产品批号
     */
    private String batchId;

    /**
     * 零售商企业编号，外键关联node_info表node_id
     */
    private Integer nodeId;

    /**
     * 进场对应的批发商批号ID(whol_batch.wb_id)
     * 后端校验：该批发商批号必须存在且state=3（已确认）
     */
    private Integer wbId;

    /**
     * 海产品品种
     */
    private String type;

    /**
     * 批号录入日期，数据库date类型
     */
    private LocalDate batchDate;

    /**
     * 溯源标识码，消费者查询使用，全局唯一
     * 批号审核通过(state=3)时自动赋值生成
     */
    private String sourceId;

    /**
     * 溯源二维码base64字符串，数据库MEDIUMTEXT存储大文本
     */
    private String sourceQr;

    /**
     * 批号状态:1新建,2待确认,3已确认,4已下架，数据库默认值1
     */
    private Integer state;

    /**
     * 备注
     */
    private String remarks;

}
