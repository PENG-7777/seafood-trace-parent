package com.peng.node.controller;

import com.peng.node.entity.RetaBatch;
import com.peng.node.service.RetaBatchService;
import com.peng.node.util.Result;
import com.peng.node.vo.SourceBatchVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 零售商批号控制器
 * 业务定位：溯源链路末端，上游批发商，无下游业务
 * 接口能力：列表查询、新增、修改、删除、下架、发送进场请求、上游批发商批号下拉
 */
@RestController
@RequestMapping("/api/node/retaBatch")
public class RetaBatchController {

    @Resource
    private RetaBatchService retaBatchService;

    /**
     * 获取当前登录零售商企业自己的批号列表
     * @param request 请求对象，拦截器注入nodeId
     * @return Result包装零售商批号集合
     */
    @GetMapping("/list")
    public Result<List<RetaBatch>> getMyBatchList(HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<RetaBatch> list = retaBatchService.getMyBatchList(nodeId);
        return Result.ok(list);
    }

    /**
     * 新增零售商批号
     * @param retaBatch 前端表单提交实体
     * @param request 请求对象
     * @return 统一成功返回
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody RetaBatch retaBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        retaBatchService.addBatch(retaBatch, nodeId);
        return Result.ok();
    }

    /**
     * 修改零售商批号，仅新建状态允许编辑
     * @param retaBatch 编辑表单数据
     * @param request 请求对象
     * @return 统一成功返回
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody RetaBatch retaBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        retaBatchService.updateBatch(retaBatch, nodeId);
        return Result.ok();
    }

    /**
     * 删除零售商批号，仅新建状态允许删除
     * @param rbId 零售商批号主键
     * @param request 请求对象
     * @return 统一成功返回
     */
    @DeleteMapping("/delete/{rbId}")
    public Result<Void> delete(@PathVariable Integer rbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        retaBatchService.deleteBatch(rbId, nodeId);
        return Result.ok();
    }

    /**
     * 零售商批号下架操作
     * @param rbId 零售商批号主键
     * @param request 请求对象
     * @return 统一成功返回
     */
    @PutMapping("/offShelve/{rbId}")
    public Result<Void> offShelve(@PathVariable Integer rbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        retaBatchService.offShelve(rbId, nodeId);
        return Result.ok();
    }

    /**
     * 零售商向上游批发商发送进场确认请求，批号状态变更为2‑待确认
     * @param rbId 零售商批号主键
     * @param request 请求对象
     * @return 统一成功返回
     */
    @PutMapping("/sendConfirmReq/{rbId}")
    public Result<Void> sendConfirmReq(@PathVariable Integer rbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        retaBatchService.sendConfirmRequest(rbId, nodeId);
        return Result.ok();
    }

    /**
     * 根据批发商企业ID，下拉查询该企业已确认可用的批发原料批号（前端二级联动）
     * @param sourceNodeId 上游批发商企业编号
     * @return 原料下拉VO列表
     */
    @GetMapping("/getUpstreamWholBatch/{sourceNodeId}")
    public Result<List<SourceBatchVO>> getUpstreamWholBatch(@PathVariable Integer sourceNodeId){
        List<SourceBatchVO> voList = retaBatchService.getUpstreamWholBatchByNodeId(sourceNodeId);
        return Result.ok(voList);
    }
}
