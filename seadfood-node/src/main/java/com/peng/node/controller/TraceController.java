package com.peng.node.controller;

import com.peng.node.service.TraceService;
import com.peng.node.util.Result;
import com.peng.node.vo.TraceVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费者公开溯源查询接口
 * 该接口需要在拦截器JwtInterceptor配置放行，不需要携带Token
 */
@RestController
@RequestMapping("/api/public/trace")
public class TraceController {

    @Resource
    private TraceService traceService;

    /**
     * 根据溯源标识码查询整条产业链溯源信息
     * @param sourceId 32位溯源编号
     * @return Result<TraceVO> 完整链路数据
     */
    @GetMapping("/query")
    public Result<TraceVO> queryTrace(@RequestParam("sourceId") String sourceId) {
        TraceVO traceVO = traceService.getTraceInfoBySourceId(sourceId);
        return Result.ok(traceVO);
    }
}
