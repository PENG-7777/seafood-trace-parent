package com.peng.node.controller;

import com.peng.node.entity.RetaBatch;
import com.peng.node.service.RetaBatchService;
import com.peng.node.util.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 零售商批号控制器
 * 接口：新增、修改、删除、下架、查询本企业批号列表、向上游批发商发送进场确认请求
 */
@RestController
@RequestMapping("/api/node/retaBatch")
public class RetaBatchController {

    @Resource
    private RetaBatchService retaBatchService;

    /**
     * 获取当前登录零售商自己的批号列表
     * @param request http请求对象，拦截器注入nodeId
     * @return Result包装零售商批号集合
     */
    @GetMapping("/list")
    public Result<List<RetaBatch>> getMyBatchList(HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<RetaBatch> list = retaBatchService.getMyBatchList(nodeId);
        return Result.ok(list);
    }

    /**
     * 新增零售商批号
     * @param retaBatch 前端提交表单实体
     * @param request http请求对象
     * @return Result
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody RetaBatch retaBatch, HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        retaBatchService.addBatch(retaBatch, nodeId);
        return Result.ok();
    }

    /**
     * 修改零售商批号（仅新建状态允许修改）
     * @param retaBatch 前端表单数据
     * @param request http请求对象
     * @return Result
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody RetaBatch retaBatch, HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        retaBatchService.updateBatch(retaBatch, nodeId);
        return Result.ok();
    }

    /**
     * 删除零售商批号（仅新建状态允许删除）
     * @param rbId 零售商批号主键
     * @param request http请求对象
     * @return Result
     */
    @DeleteMapping("/delete/{rbId}")
    public Result<Void> delete(@PathVariable Integer rbId, HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        retaBatchService.deleteBatch(rbId, nodeId);
        return Result.ok();
    }

    /**
     * 批号下架，状态改为4‑已下架
     * @param rbId 零售商批号主键
     * @param request http请求对象
     * @return Result
     */
    @PutMapping("/offShelve/{rbId}")
    public Result<Void> offShelve(@PathVariable Integer rbId, HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        retaBatchService.offShelve(rbId, nodeId);
        return Result.ok();
    }

    /**
     * 零售商向上游批发商发送进场确认请求
     * 批号状态由1‑新建变更为2‑待确认，等待批发商审核确认
     * @param rbId 零售商批号主键
     * @param request http请求对象
     * @return Result
     */
    @PutMapping("/sendConfirmReq/{rbId}")
    public Result<Void> sendConfirmReq(@PathVariable Integer rbId, HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        retaBatchService.sendConfirmRequest(rbId, nodeId);
        return Result.ok();
    }
}
