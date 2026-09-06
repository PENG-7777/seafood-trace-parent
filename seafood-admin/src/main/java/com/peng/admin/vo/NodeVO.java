package com.peng.admin.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 企业节点返回VO对象
 * 用于分页查询、列表展示，返回给前端的企业详情数据
 * 不返回密码等敏感字段，携带省份、城市中文名称
 */
@Data
public class NodeVO {

    /**
     * 节点企业编号
     */
    private Integer nodeId;

    /**
     * 流通端登录编码(账号)
     */
    private String code;

    /**
     * 节点企业名称
     */
    private String name;

    /**
     * 企业类型：1捕捞企业,2养殖企业,3批发商,4零售商
     */
    private Integer type;

    /**
     * 企业类型中文名称
     */
    private String typeName;

    /**
     * 所属省份编号
     */
    private Integer provId;

    /**
     * 省份中文名称
     */
    private String provName;

    /**
     * 所属城市编号
     */
    private Integer cityId;

    /**
     * 城市中文名称
     */
    private String cityName;

    /**
     * 企业详细地址
     */
    private String address;

    /**
     * 营业执照编号
     */
    private String businessId;

    /**
     * 渔业捕捞许可证编号
     */
    private String fishingLic;

    /**
     * 水域滩涂养殖证编号
     */
    private String aquacultureLic;

    /**
     * 食品经营许可证编号
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
     * 企业注册日期
     */
    private LocalDate regDate;

    /**
     * 备注信息
     */
    private String remarks;

}

