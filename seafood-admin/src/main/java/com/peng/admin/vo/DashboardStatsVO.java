package com.peng.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * 管理员端数据统计汇总VO
 * 前端一次请求获取全部统计图表数据
 */
@Data
public class DashboardStatsVO {

    /** 企业总数 */
    private Long totalCount;

    /** 1.企业注册数量趋势（按天） */
    private List<RegisterTrendVO> registerTrend;

    /** 2.省分组注册数量分布 */
    private List<ProvStatsVO> provDistribution;

    /** 3.企业类型分组注册数量分布 */
    private List<TypeStatsVO> typeDistribution;

    /** 4.省分组注册数量统计（带总数/占比） */
    private List<ProvStatsVO> provStats;
}
