package com.peng.node.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * 节点企业实体类
 * 对应数据库表：node_info 节点企业信息表：捕捞企业、养殖企业、批发商、零售商
 * 企业类型：1捕捞企业,2养殖企业,3批发商,4零售商
 */
@Data
@TableName("node_info")
public class NodeInfo {

    /**
     * 节点企业编号，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer nodeId;

    /**
     * 流通端登录编码(账号)，唯一约束
     */
    private String code;

    /**
     * 登录密码(BCrypt加密存储，密文60位)
     */
    private String password;

    /**
     * 节点企业名称
     */
    private String name;

    /**
     * 企业类型:1捕捞企业,2养殖企业,3冷冻企业,4批发商,5零售商
     */
    private Integer type;

    /**
     * 所属省份编号，外键关联province表prov_id
     */
    private Integer provId;

    /**
     * 所属城市编号，外键关联city表city_id
     */
    private Integer cityId;

    /**
     * 企业详细地址
     */
    private String address;

    /**
     * 营业执照编号
     */
    private String businessId;

    /**
     * 渔业捕捞许可证编号(捕捞企业必填)
     */
    private String fishingLic;

    /**
     * 水域滩涂养殖证编号(养殖企业必填)
     */
    private String aquacultureLic;

    /**
     * 食品经营许可证编号(批发、零售企业必填)
     */
    private String foodBusinessLic;

    /**
     * 法定代表人
     */
    private String corporation;

    /**
     * 企业联系电话
     */
    private String telephone;

    /**
     * 企业注册日期，数据库date类型，使用LocalDate
     */
    private LocalDate regDate;

    /**
     * 备注信息
     */
    private String remarks;
}

