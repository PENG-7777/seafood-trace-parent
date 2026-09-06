package com.peng.admin.mapper;

import com.peng.admin.vo.ProvStatsVO;
import com.peng.admin.vo.RegisterTrendVO;
import com.peng.admin.vo.TypeStatsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据统计Mapper
 */
public interface NodeStatsMapper {

    /**
     * 1.企业注册数量趋势（按注册日期分组）
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     */
    List<RegisterTrendVO> selectRegisterTrend(@Param("startDate") String startDate,
                                              @Param("endDate") String endDate);

    /**
     * 2.省分组注册数量分布
     */
    List<ProvStatsVO> selectProvDistribution();

    /**
     * 3.企业类型分组注册数量分布
     */
    List<TypeStatsVO> selectTypeDistribution();

    /**
     * 4.省分组注册数量统计
     */
    List<ProvStatsVO> selectProvStats();
}
