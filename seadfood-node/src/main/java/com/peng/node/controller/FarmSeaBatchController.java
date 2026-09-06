package com.peng.node.controller;

import com.peng.node.entity.FarmSeaBatch;
import com.peng.node.service.FarmSeaBatchService;
import com.peng.node.util.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 海水养殖企业批号控制器
 * 提供养殖批号新增、修改、删除、下架、列表、确认冷冻加工进场接口
 */
@RestController
@RequestMapping("/api/node/farmSeaBatch")
public class FarmSeaBatchController {

    @Resource
    private FarmSeaBatchService farmSeaBatchService;

    /**
     * 获取当前养殖企业自己的批号列表
     * @param request http请求，获取登录企业nodeId
     * @return Result包装批号列表
     */
    @GetMapping("/list")
    public Result<List<FarmSeaBatch>> getMyBatchList(HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<FarmSeaBatch> list = farmSeaBatchService.getMyBatchList(nodeId);
        return Result.ok(list);
    }

    /**
     * 新增养殖批号
     * @param farmSeaBatch 前端表单批号数据
     * @param request http请求，获取登录企业nodeId
     * @return Result
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody FarmSeaBatch farmSeaBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        farmSeaBatchService.addBatch(farmSeaBatch, nodeId);
        return Result.ok();
    }

    /**
     * 修改养殖批号
     * @param farmSeaBatch 表单数据
     * @param request http请求
     * @return Result
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody FarmSeaBatch farmSeaBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        farmSeaBatchService.updateBatch(farmSeaBatch, nodeId);
        return Result.ok();
    }

    /**
     * 删除养殖批号
     * @param fsbId 批号主键
     * @param request http请求
     * @return Result
     */
    @DeleteMapping("/delete/{fsbId}")
    public Result<Void> delete(@PathVariable Integer fsbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        farmSeaBatchService.deleteBatch(fsbId, nodeId);
        return Result.ok();
    }

    /**
     * 批号下架
     * @param fsbId 批号主键
     * @param request http请求
     * @return Result
     */
    @PutMapping("/offShelve/{fsbId}")
    public Result<Void> offShelve(@PathVariable Integer fsbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        farmSeaBatchService.offShelve(fsbId, nodeId);
        return Result.ok();
    }

    /**
     * 养殖企业确认冷冻加工企业进场请求
     * @param processBatchId 冷冻加工批号主键
     * @param request http请求
     * @return Result
     */
    @PutMapping("/confirm/{processBatchId}")
    public Result<Void> confirm(@PathVariable Integer processBatchId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        farmSeaBatchService.confirmProcessBatch(processBatchId, nodeId);
        return Result.ok();
    }
}
