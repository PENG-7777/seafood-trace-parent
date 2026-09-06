package com.peng.node.controller;

import com.peng.node.entity.ProcessBatch;
import com.peng.node.service.ProcessBatchService;
import com.peng.node.util.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 冷冻加工企业批号控制器
 * 接口：新增、修改、删除、下架、列表、发送进场确认请求、确认批发商进场
 */
@RestController
@RequestMapping("/api/node/processBatch")
public class ProcessBatchController {

    @Resource
    private ProcessBatchService processBatchService;

    /**
     * 获取当前冷冻加工企业自己的批号列表
     * @param request http请求，获取登录企业nodeId
     * @return Result包装批号列表
     */
    @GetMapping("/list")
    public Result<List<ProcessBatch>> getMyBatchList(HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<ProcessBatch> list = processBatchService.getMyBatchList(nodeId);
        return Result.ok(list);
    }

    /**
     * 新增冷冻加工批号
     * @param processBatch 前端表单数据
     * @param request http请求
     * @return Result
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody ProcessBatch processBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.addBatch(processBatch, nodeId);
        return Result.ok();
    }

    /**
     * 修改冷冻加工批号
     * @param processBatch 表单数据
     * @param request http请求
     * @return Result
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody ProcessBatch processBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.updateBatch(processBatch, nodeId);
        return Result.ok();
    }

    /**
     * 删除冷冻加工批号
     * @param pbId 加工批号主键
     * @param request http请求
     * @return Result
     */
    @DeleteMapping("/delete/{pbId}")
    public Result<Void> delete(@PathVariable Integer pbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.deleteBatch(pbId, nodeId);
        return Result.ok();
    }

    /**
     * 批号下架
     * @param pbId 加工批号主键
     * @param request http请求
     * @return Result
     */
    @PutMapping("/offShelve/{pbId}")
    public Result<Void> offShelve(@PathVariable Integer pbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.offShelve(pbId, nodeId);
        return Result.ok();
    }

    /**
     * 向上游源头企业发送进场确认请求，批号状态变为待确认
     * @param pbId 加工批号主键
     * @param request http请求
     * @return Result
     */
    @PutMapping("/sendConfirmReq/{pbId}")
    public Result<Void> sendConfirmReq(@PathVariable Integer pbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.sendConfirmRequest(pbId, nodeId);
        return Result.ok();
    }

    /**
     * 冷冻加工企业确认批发商进场请求
     * @param wholBatchId 批发商批号主键
     * @param request http请求
     * @return Result
     */
    @PutMapping("/confirm/{wholBatchId}")
    public Result<Void> confirmWholBatch(@PathVariable Integer wholBatchId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.confirmWholBatch(wholBatchId, nodeId);
        return Result.ok();
    }
}
