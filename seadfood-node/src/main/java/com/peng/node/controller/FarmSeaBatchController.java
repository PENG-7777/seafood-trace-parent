package com.peng.node.controller;

import com.peng.node.entity.FarmSeaBatch;
import com.peng.node.entity.ProcessBatch;
import com.peng.node.service.FarmSeaBatchService;
import com.peng.node.util.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 海水养殖企业批号控制器
 * 提供养殖批号新增、修改、删除、下架、列表、获取加工进场申请列表、确认冷冻加工进场接口
 * 请求统一前缀：/api/node/farmSeaBatch
 */
@RestController
@RequestMapping("/api/node/farmSeaBatch")
public class FarmSeaBatchController {

    @Resource
    private FarmSeaBatchService farmSeaBatchService;

    /**
     * 获取当前登录养殖企业自身的养殖批号列表
     * @param request http请求对象，拦截器注入当前登录企业nodeId
     * @return Result包装的养殖批号集合
     */
    @GetMapping("/list")
    public Result<List<FarmSeaBatch>> getMyBatchList(HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<FarmSeaBatch> list = farmSeaBatchService.getMyBatchList(nodeId);
        return Result.ok(list);
    }

    /**
     * 新增海水养殖批号
     * @param farmSeaBatch 前端提交的养殖批号表单实体
     * @param request http请求对象，拦截器注入当前登录企业nodeId
     * @return Result统一返回体，无返回数据
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody FarmSeaBatch farmSeaBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        farmSeaBatchService.addBatch(farmSeaBatch, nodeId);
        return Result.ok();
    }

    /**
     * 修改已有养殖批号信息
     * @param farmSeaBatch 编辑后的批号完整实体
     * @param request http请求对象，拦截器注入当前登录企业nodeId（做数据权限校验）
     * @return Result统一返回体
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody FarmSeaBatch farmSeaBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        farmSeaBatchService.updateBatch(farmSeaBatch, nodeId);
        return Result.ok();
    }

    /**
     * 删除指定养殖批号
     * @param fsbId 养殖批号主键ID
     * @param request http请求对象，获取登录企业编号防止越权删除
     * @return Result统一返回体
     */
    @DeleteMapping("/delete/{fsbId}")
    public Result<Void> delete(@PathVariable Integer fsbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        farmSeaBatchService.deleteBatch(fsbId, nodeId);
        return Result.ok();
    }

    /**
     * 养殖批号下架操作，停止对外供给加工
     * @param fsbId 养殖批号主键
     * @param request http请求对象，获取登录企业编号校验权限
     * @return Result统一返回体
     */
    @PutMapping("/offShelve/{fsbId}")
    public Result<Void> offShelve(@PathVariable Integer fsbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        farmSeaBatchService.offShelve(fsbId, nodeId);
        return Result.ok();
    }

    /**
     * 查询待确认的冷冻加工进场申请列表（本次报错缺失的接口）
     * @param request http请求对象，获取当前登录养殖企业nodeId
     * @return Result包装加工申请数据集合，附带加工企业nodeName
     */
    @GetMapping("/getProcessApplyList")
    public Result<List<ProcessBatch>> getProcessApplyList(HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<ProcessBatch> applyList = farmSeaBatchService.getProcessApplyList(nodeId);
        return Result.ok(applyList);
    }

    /**
     * 养殖企业审核确认冷冻加工企业的进场申请
     * @param processBatchId 冷冻加工批号主键ID
     * @param request http请求对象，获取当前登录养殖企业nodeId，校验操作权限
     * @return Result统一返回体
     */
    @PutMapping("/confirm/{processBatchId}")
    public Result<Void> confirm(@PathVariable Integer processBatchId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        farmSeaBatchService.confirmProcessBatch(processBatchId, nodeId);
        return Result.ok();
    }

}
