package com.peng.admin.vo;

import lombok.Data;

/**
 * 企业注册数量趋势VO
 * 按注册日期分组统计
 */
@Data
public class RegisterTrendVO {

    /** 注册日期 yyyy-MM-dd */
    private String registerDate;

    /** 该日注册企业数量 */
    private Long count;
}
