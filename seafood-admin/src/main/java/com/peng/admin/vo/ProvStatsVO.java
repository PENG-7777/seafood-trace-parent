package com.peng.admin.vo;

import lombok.Data;

/**
 * 省分组注册数量分布/统计VO
 */
@Data
public class ProvStatsVO {

    /** 省份编号 */
    private Integer provId;

    /** 省份名称 */
    private String provName;

    /** 该省注册企业数量 */
    private Long count;
}
