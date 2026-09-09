package com.peng.node.controller;

import com.peng.node.entity.WholBatch;
import com.peng.node.service.WholBatchService;
import com.peng.node.util.Result;
import com.peng.node.vo.RetailApplyVO;
import com.peng.node.vo.SourceBatchVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 批发商批号控制器
 * 业务链路：上游冷冻加工企业，下游零售商
 * 接口能力：列表、新增、修改、删除、下架、发送进场请求、确认零售商进场、上游冷冻加工批号下拉
 */
@RestController
@RequestMapping("/api/node/wholBatch")
public class WholBatchController {

    @Resource
    private WholBatchService wholBatchService;

    /**
     * 获取当前登录批发商企业自己的批号列表
     * @param request http请求，拦截器注入nodeId
     * @return Result包装批发商批号集合
     */
    @GetMapping("/list")
    public Result<List<WholBatch>> getMyBatchList(HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<WholBatch> list = wholBatchService.getMyBatchList(nodeId);
        return Result.ok(list);
    }

    /**
     * 新增批发商批号
     * @param wholBatch 前端表单提交实体
     * @param request http请求对象
     * @return 统一成功返回
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody WholBatch wholBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.addBatch(wholBatch, nodeId);
        return Result.ok();
    }

    /**
     * 修改批发商批号，仅新建状态允许编辑
     * @param wholBatch 编辑表单数据
     * @param request http请求对象
     * @return 统一成功返回
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody WholBatch wholBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.updateBatch(wholBatch, nodeId);
        return Result.ok();
    }

    /**
     * 删除批发商批号，仅新建状态允许删除
     * @param wbId 批发商批号主键
     * @param request http请求对象
     * @return 统一成功返回
     */
    @DeleteMapping("/delete/{wbId}")
    public Result<Void> delete(@PathVariable Integer wbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.deleteBatch(wbId, nodeId);
        return Result.ok();
    }

    /**
     * 批发商批号下架操作
     * @param wbId 批发商批号主键
     * @param request http请求对象
     * @return 统一成功返回
     */
    @PutMapping("/offShelve/{wbId}")
    public Result<Void> offShelve(@PathVariable Integer wbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.offShelve(wbId, nodeId);
        return Result.ok();
    }

    /**
     * 批发商向上游冷冻加工企业发送进场确认请求，批号状态变更为待确认
     * @param wbId 批发商批号主键
     * @param request http请求对象
     * @return 统一成功返回
     */
    @PutMapping("/sendConfirmReq/{wbId}")
    public Result<Void> sendConfirmReq(@PathVariable Integer wbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.sendConfirmRequest(wbId, nodeId);
        return Result.ok();
    }

    /**
     * 批发商审核确认零售商进场申请，生成溯源码与二维码
     * @param retaBatchId 零售商批号主键
     * @param request http请求对象
     * @return 统一成功返回
     */
    @PutMapping("/confirm/{retaBatchId}")
    public Result<Void> confirmRetaBatch(@PathVariable Integer retaBatchId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        wholBatchService.confirmRetaBatch(retaBatchId, nodeId);
        return Result.ok();
    }

    /**
     * 根据冷冻加工企业ID，下拉查询该企业已确认的成品批号（前端联动选择上游原料）
     * @param sourceNodeId 上游冷冻加工企业编号
     * @return 原料下拉VO列表
     */
    @GetMapping("/getUpstreamProcessBatch/{sourceNodeId}")
    public Result<List<SourceBatchVO>> getUpstreamProcessBatch(@PathVariable Integer sourceNodeId){
        List<SourceBatchVO> voList = wholBatchService.getUpstreamProcessBatchByNodeId(sourceNodeId);
        return Result.ok(voList);
    }

    /**
     * 获取提交给当前批发商的零售商进场申请列表
     * @param request 请求对象，拦截器注入nodeId
     * @return 零售商进场申请VO列表
     */
    @GetMapping("/getRetaApplyList")
    public Result<List<RetailApplyVO>> getRetaApplyList(HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<RetailApplyVO> voList = wholBatchService.getRetaApplyList(nodeId);
        return Result.ok(voList);
    }

}
