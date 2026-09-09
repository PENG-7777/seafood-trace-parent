package com.peng.node.controller;

import com.peng.node.entity.ProcessBatch;
import com.peng.node.service.ProcessBatchService;
import com.peng.node.util.Result;
import com.peng.node.vo.SourceBatchVO;
import com.peng.node.vo.WholApplyVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 冷冻加工企业批号控制器
 * 接口能力：新增、修改、删除、下架、企业自有批号列表、发送进场确认请求、确认批发商进场、上游原料批号下拉查询
 */
@RestController
@RequestMapping("/api/node/processBatch")
public class ProcessBatchController {

    @Resource
    private ProcessBatchService processBatchService;

    /**
     * 获取当前登录冷冻加工企业自己的全部批号列表
     * @param request http请求对象，从请求域中获取拦截器存放的登录企业nodeId
     * @return Result包装加工批号集合
     */
    @GetMapping("/list")
    public Result<List<ProcessBatch>> getMyBatchList(HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<ProcessBatch> list = processBatchService.getMyBatchList(nodeId);
        return Result.ok(list);
    }

    /**
     * 新增冷冻加工产品批号
     * @param processBatch 前端提交的加工批号表单实体
     * @param request http请求对象，获取当前登录企业nodeId用于数据权限控制
     * @return 统一返回成功结果
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody ProcessBatch processBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.addBatch(processBatch, nodeId);
        return Result.ok();
    }

    /**
     * 修改冷冻加工批号信息，仅待发布状态允许编辑
     * @param processBatch 编辑提交的批号实体
     * @param request http请求对象，获取当前登录企业nodeId做权限校验
     * @return 统一返回成功结果
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody ProcessBatch processBatch, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.updateBatch(processBatch, nodeId);
        return Result.ok();
    }

    /**
     * 删除冷冻加工批号，仅待发布状态允许删除
     * @param pbId 加工批号主键ID
     * @param request http请求对象，获取当前登录企业nodeId做数据权限校验
     * @return 统一返回成功结果
     */
    @DeleteMapping("/delete/{pbId}")
    public Result<Void> delete(@PathVariable Integer pbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.deleteBatch(pbId, nodeId);
        return Result.ok();
    }

    /**
     * 冷冻加工批号下架操作，修改批号状态为已下架
     * @param pbId 加工批号主键ID
     * @param request http请求对象，获取当前登录企业nodeId做权限校验
     * @return 统一返回成功结果
     */
    @PutMapping("/offShelve/{pbId}")
    public Result<Void> offShelve(@PathVariable Integer pbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.offShelve(pbId, nodeId);
        return Result.ok();
    }

    /**
     * 向源头上游企业（捕捞/海水养殖）发送进场确认请求，批号状态变更为待确认
     * @param pbId 加工批号主键ID
     * @param request http请求对象，获取当前登录企业nodeId做权限校验
     * @return 统一返回成功结果
     */
    @PutMapping("/sendConfirmReq/{pbId}")
    public Result<Void> sendConfirmReq(@PathVariable Integer pbId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.sendConfirmRequest(pbId, nodeId);
        return Result.ok();
    }

    /**
     * 冷冻加工企业确认批发商的进场申请，更新批发商批号状态为已确认
     * @param wholBatchId 批发商批号主键ID
     * @param request http请求对象，获取当前登录冷冻加工企业nodeId用于权限校验
     * @return 统一返回成功结果
     */
    @PutMapping("/confirm/{wholBatchId}")
    public Result<Void> confirmWholBatch(@PathVariable Integer wholBatchId, HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        processBatchService.confirmWholBatch(wholBatchId, nodeId);
        return Result.ok();
    }

    /**
     * 根据上游企业ID，下拉查询该企业所有【已发布】的原料批号（联动下拉选择原料）
     * 上游分为捕捞fish_batch、海水养殖farm_sea_batch两类源头批号
     * @param sourceNodeId 上游源头企业编号
     * @return Result包装原料下拉VO列表
     */
    @GetMapping("/getUpstreamBatch/{sourceNodeId}")
    public Result<List<SourceBatchVO>> getUpstreamBatch(@PathVariable Integer sourceNodeId){
        List<SourceBatchVO> voList = processBatchService.getUpstreamBatchByNodeId(sourceNodeId);
        return Result.ok(voList);
    }

    /**
     * 查询当前冷冻加工企业收到的下游批发企业进场申请列表
     * @param request 获取当前登录加工企业nodeId作为上游权限过滤条件
     * @return 批发进场申请VO集合
     */
    @GetMapping("/getWholApplyList")
    public Result<List<WholApplyVO>> getWholApplyList(HttpServletRequest request){
        Integer nodeId = (Integer) request.getAttribute("nodeId");
        List<WholApplyVO> voList = processBatchService.getWholApplyList(nodeId);
        return Result.ok(voList);
    }


}
