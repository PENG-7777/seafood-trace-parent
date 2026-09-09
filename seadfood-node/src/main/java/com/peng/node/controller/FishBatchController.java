package com.peng.node.controller;

import com.peng.node.entity.FishBatch;
import com.peng.node.entity.NodeInfo;
import com.peng.node.vo.ProcessApplyVO;
import com.peng.node.service.FishBatchService;
import com.peng.node.util.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 捕捞企业批号控制器
 * 提供捕捞批号新增、修改、删除、下架、列表、确认冷冻加工进场接口
 */
@RestController
@RequestMapping("/api/node/fishBatch")
public class FishBatchController {

    @Resource
    private FishBatchService fishBatchService;

    /**
     * 获取当前捕捞企业自己的批号列表
     * @param request http请求，获取登录企业nodeId
     * @return Result包装批号列表
     */
    @GetMapping("/list")
    public Result<List<FishBatch>> getMyBatchList(HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<FishBatch> list = fishBatchService.getMyBatchList(nodeId);
        return Result.ok(list);
    }

    /**
     * 【新增接口】获取下游加工企业进场申请列表
     * @param request 获取当前登录捕捞企业nodeId
     * @return 进场申请VO集合
     */
    @GetMapping("/getProcessApplyList")
    public Result<List<ProcessApplyVO>> getProcessApplyList(HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<ProcessApplyVO> applyList = fishBatchService.getProcessApplyList(nodeId);
        return Result.ok(applyList);
    }

    /**
     * 新增捕捞批号
     * @param fishBatch 前端表单批号数据
     * @param request http请求，获取登录企业nodeId
     * @return Result
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody FishBatch fishBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        fishBatchService.addBatch(fishBatch, nodeId);
        return Result.ok();
    }

    /**
     * 修改捕捞批号
     * @param fishBatch 表单数据
     * @param request http请求
     * @return Result
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody FishBatch fishBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        fishBatchService.updateBatch(fishBatch, nodeId);
        return Result.ok();
    }

    /**
     * 删除捕捞批号
     * @param fbId 批号主键
     * @param request http请求
     * @return Result
     */
    @DeleteMapping("/delete/{fbId}")
    public Result<Void> delete(@PathVariable Integer fbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        fishBatchService.deleteBatch(fbId, nodeId);
        return Result.ok();
    }

    /**
     * 批号下架
     * @param fbId 批号主键
     * @param request http请求
     * @return Result
     */
    @PutMapping("/offShelve/{fbId}")
    public Result<Void> offShelve(@PathVariable Integer fbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        fishBatchService.offShelve(fbId, nodeId);
        return Result.ok();
    }

    /**
     * 捕捞企业确认冷冻加工企业进场请求
     * @param processBatchId 冷冻加工批号主键
     * @param request http请求
     * @return Result
     */
    @PutMapping("/confirm/{processBatchId}")
    public Result<Void> confirm(@PathVariable Integer processBatchId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        fishBatchService.confirmProcessBatch(processBatchId, nodeId);
        return Result.ok();
    }

}
