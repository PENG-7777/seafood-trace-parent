package com.peng.admin.controller;

import com.peng.admin.common.Result;
import com.peng.admin.service.NodeStatsService;
import com.peng.admin.util.ResultUtil;
import com.peng.admin.vo.DashboardStatsVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * 数据统计控制器
 * 接口前缀：/api/node/stats
 */
@RestController
@RequestMapping("/api/node/stats")
public class NodeStatsController {

    @Resource
    private NodeStatsService nodeStatsService;

    /**
     * 管理员端数据统计总览
     * 返回企业注册趋势、省分布、类型分布、省统计
     * @param startDate 趋势开始日期（可选）yyyy-MM-dd
     * @param endDate 趋势结束日期（可选）yyyy-MM-dd
     */
    @GetMapping("/dashboard")
    public Result<DashboardStatsVO> dashboard(@RequestParam(required = false) String startDate,
                                              @RequestParam(required = false) String endDate) {
        DashboardStatsVO vo = nodeStatsService.getDashboardStats(startDate, endDate);
        return ResultUtil.success(vo);
    }
}
