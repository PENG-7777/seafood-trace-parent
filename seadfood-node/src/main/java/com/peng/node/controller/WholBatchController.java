package com.peng.node.controller;

import com.peng.node.entity.WholBatch;
import com.peng.node.service.WholBatchService;
import com.peng.node.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 批发商批号控制器
 * 接口：新增、修改、删除、下架、查询本企业批号列表、向上游发送进场请求、确认零售商进场请求
 */
@RestController
@RequestMapping("/api/node/wholBatch")
public class WholBatchController {

    @Resource
    private WholBatchService wholBatchService;

    /**
     * 获取当前登录批发商自己的批号列表
     * @param request http请求对象，从request域获取登录企业nodeId
     * @return Result包装批发商批号集合
     */
    @GetMapping("/list")
    public Result<List<WholBatch>> getMyBatchList(HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<WholBatch> list = wholBatchService.getMyBatchList(nodeId);
        return Result.ok(list);
    }

    /**
     * 新增批发商批号
     * @param wholBatch 前端表单提交的批发商批号实体
     * @param request http请求对象，获取登录企业nodeId
     * @return Result
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody WholBatch wholBatch, HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.addBatch(wholBatch, nodeId);
        return Result.ok();
    }

    /**
     * 修改批发商批号
     * 仅状态为【1‑新建】允许修改
     * @param wholBatch 前端表单数据
     * @param request http请求对象，获取登录企业nodeId
     * @return Result
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody WholBatch wholBatch, HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.updateBatch(wholBatch, nodeId);
        return Result.ok();
    }

    /**
     * 删除批发商批号
     * 仅状态为【1‑新建】允许删除
     * @param wbId 批发商批号主键wbId
     * @param request http请求对象，获取登录企业nodeId
     * @return Result
     */
    @DeleteMapping("/delete/{wbId}")
    public Result<Void> delete(@PathVariable Integer wbId, HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.deleteBatch(wbId, nodeId);
        return Result.ok();
    }

    /**
     * 批号下架，状态修改为4‑已下架
     * @param wbId 批发商批号主键wbId
     * @param request http请求对象，获取登录企业nodeId
     * @return Result
     */
    @PutMapping("/offShelve/{wbId}")
    public Result<Void> offShelve(@PathVariable Integer wbId, HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.offShelve(wbId, nodeId);
        return Result.ok();
    }

    /**
     * 批发商向上游冷冻加工企业发送进场确认请求
     * 批号状态由1‑新建修改为2‑待确认，等待上游冷冻加工企业确认
     * @param wbId 批发商批号主键wbId
     * @param request http请求对象，获取登录企业nodeId
     * @return Result
     */
    @PutMapping("/sendConfirmReq/{wbId}")
    public Result<Void> sendConfirmReq(@PathVariable Integer wbId, HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.sendConfirmRequest(wbId, nodeId);
        return Result.ok();
    }

    /**
     * 批发商确认零售商进场请求
     * 事务：更新零售商批号状态，自动生成溯源标识码sourceId
     * @param retaBatchId 零售商批号主键
     * @param request http请求对象，获取登录企业nodeId
     * @return Result
     */
    @PutMapping("/confirm/{retaBatchId}")
    public Result<Void> confirmRetaBatch(@PathVariable Integer retaBatchId, HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.confirmRetaBatch(retaBatchId, nodeId);
        return Result.ok();
    }
}
