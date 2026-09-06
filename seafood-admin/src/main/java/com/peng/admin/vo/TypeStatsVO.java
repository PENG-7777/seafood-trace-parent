package com.peng.admin.vo;

import lombok.Data;

/**
 * 企业类型分组注册数量分布VO
 */
@Data
public class TypeStatsVO {

    /** 企业类型：1捕捞企业,2养殖企业,3冷冻加工企业,4批发商,5零售商*/
    private Integer type;

    /** 企业类型中文名称 */
    private String typeName;

    /** 该类型企业数量 */
    private Long count;
}
