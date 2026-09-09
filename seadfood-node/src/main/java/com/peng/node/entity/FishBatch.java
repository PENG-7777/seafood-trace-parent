package com.peng.node.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * 捕捞企业产品批号实体类
 * 对应数据库表：fish_batch 捕捞企业产品批号表（源头捕捞）
 * 批号状态：1待发布，2已发布，3已下架
 */
@Data
@TableName("fish_batch")
public class FishBatch {

    /**
     * 捕捞批号记录主键ID，自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer fbId;

    /**
     * 捕捞企业业务产品批号
     */
    private String batchId;

    /**
     * 所属捕捞企业编号，外键关联node_info表node_id
     */
    private Integer nodeId;

    /**
     * 海产品品种
     */
    private String type;

    /**
     * 渔业捕捞许可证编号
     */
    private String fishLicId;

    /**
     * 官方检疫人员名称
     */
    private String testName;

    /**
     * 捕捞海域
     */
    private String seaArea;

    /**
     * 捕捞出海日期，数据库date类型
     */
    private LocalDate batchDate;

    /**
     * 批号状态:1待发布,2已发布,3已下架，默认值1
     */
    private Integer state;

    /**
     * 备注
     */
    private String remarks;

    @TableField(exist = false)
    private String nodeName;
}
