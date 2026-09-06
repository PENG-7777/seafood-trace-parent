package com.peng.admin.service;

import com.peng.admin.vo.DashboardStatsVO;

/**
 * 数据统计业务接口
 */
public interface NodeStatsService {

    /**
     * 获取管理员端数据统计总览
     * @param startDate 趋势开始日期 yyyy-MM-dd（可选）
     * @param endDate 趋势结束日期 yyyy-MM-dd（可选）
     */
    DashboardStatsVO getDashboardStats(String startDate, String endDate);
}
