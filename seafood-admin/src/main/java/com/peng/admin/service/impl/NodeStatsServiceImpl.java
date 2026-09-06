package com.peng.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.peng.admin.entity.NodeInfo;
import com.peng.admin.mapper.NodeInfoMapper;
import com.peng.admin.mapper.NodeStatsMapper;
import com.peng.admin.service.NodeStatsService;
import com.peng.admin.vo.DashboardStatsVO;
import com.peng.admin.vo.ProvStatsVO;
import com.peng.admin.vo.RegisterTrendVO;
import com.peng.admin.vo.TypeStatsVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据统计业务实现类
 */
@Service
public class NodeStatsServiceImpl implements NodeStatsService {

    @Resource
    private NodeStatsMapper nodeStatsMapper;

    @Resource
    private NodeInfoMapper nodeInfoMapper;

    @Override
    public DashboardStatsVO getDashboardStats(String startDate, String endDate) {
        DashboardStatsVO vo = new DashboardStatsVO();

        // 企业总数
        Long total = nodeInfoMapper.selectCount(new LambdaQueryWrapper<>());
        vo.setTotalCount(total);

        // 1.企业注册数量趋势
        List<RegisterTrendVO> trend = nodeStatsMapper.selectRegisterTrend(startDate, endDate);
        vo.setRegisterTrend(trend);

        // 2.省分组注册数量分布
        List<ProvStatsVO> provDistribution = nodeStatsMapper.selectProvDistribution();
        vo.setProvDistribution(provDistribution);

        // 3.企业类型分组注册数量分布（补充中文类型名）
        List<TypeStatsVO> typeDistribution = nodeStatsMapper.selectTypeDistribution();
        typeDistribution.forEach(this::fillTypeName);
        vo.setTypeDistribution(typeDistribution);

        // 4.省分组注册数量统计
        List<ProvStatsVO> provStats = nodeStatsMapper.selectProvStats();
        vo.setProvStats(provStats);

        return vo;
    }

    /**
     * 将type数字翻译为中文类型名
     */
    private void fillTypeName(TypeStatsVO vo) {
        String typeName = switch (vo.getType()) {
            case 1 -> "捕捞企业";
            case 2 -> "养殖企业";
            case 3 -> "冷冻加工企业";
            case 4 -> "批发商";
            default -> "零售商";
        };
        vo.setTypeName(typeName);
    }
}
