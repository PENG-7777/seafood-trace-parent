package com.peng.node.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;

/**
 * 海水养殖企业产品批号实体类
 * 对应数据库表：farm_sea_batch 海水养殖企业产品批号表（源头海水养殖）
 * 批号状态：1待发布，2已发布，3已下架
 */
@Data
@TableName("farm_sea_batch")
public class FarmSeaBatch {

    /**
     * 养殖批号记录主键ID，自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer fsbId;

    /**
     * 海水养殖企业业务产品批号
     */
    private String batchId;

    /**
     * 所属养殖企业编号，外键关联node_info表node_id
     */
    private Integer nodeId;

    /**
     * 海产品品种
     */
    private String type;

    /**
     * 水域滩涂养殖证编号
     */
    private String aquaLicId;

    /**
     * 检疫人员名称
     */
    private String testName;

    /**
     * 养殖基地地址
     */
    private String farmAddr;

    /**
     * 海产品出栏日期，数据库date类型
     */
    private LocalDate batchDate;

    /**
     * 批号状态:1待发布,2已发布,3已下架，数据库默认值1
     */
    private Integer state;

    /**
     * 备注
     */
    private String remarks;

    @TableField(exist = false)
    private String nodeName;
}
