package com.peng.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.peng.admin.common.Result;
import com.peng.admin.dto.NodeSaveDTO;
import com.peng.admin.service.NodeInfoService;
import com.peng.admin.util.ResultUtil;
import com.peng.admin.vo.NodeVO;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



/**
 * 流通节点企业管理控制器
 * 接口前缀：/api/node
 */
@RestController
@RequestMapping("/api/node")
public class NodeController {

    @Resource
    private NodeInfoService nodeInfoService;

    /**
     * 企业分页列表查询
     * @param pageNum 页码，默认1
     * @param pageSize 每页条数，默认10
     * @param name 企业名称模糊检索（可选）
     * @param type 企业类型筛选（可选：1捕捞/2养殖/3批发/4零售）
     * @return 分页VO数据
     */
    @GetMapping("/page")
    public Result<IPage<NodeVO>> page(@RequestParam(defaultValue = "1") Long pageNum,
                                      @RequestParam(defaultValue = "10") Long pageSize,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) Integer type) {
        IPage<NodeVO> pageResult = nodeInfoService.getNodePage(pageNum, pageSize, name, type);
        return ResultUtil.success(pageResult);
    }

    /**
     * 新增 / 编辑保存企业信息
     * @param dto 保存表单数据
     * @return 无返回数据
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated NodeSaveDTO dto) {
        nodeInfoService.saveOrUpdateNode(dto);
        return ResultUtil.success();
    }

    /**
     * 根据ID查询企业详情
     * @param nodeId 企业主键id
     * @return NodeVO详情对象
     */
    @GetMapping("/detail/{nodeId}")
    public Result<NodeVO> detail(@PathVariable Integer nodeId) {
        NodeVO vo = nodeInfoService.getNodeDetailById(nodeId);
        return ResultUtil.success(vo);
    }

    /**
     * 根据ID删除企业
     * @param nodeId 企业主键id
     * @return 操作结果
     */
    @DeleteMapping("/delete/{nodeId}")
    public Result<Void> delete(@PathVariable Integer nodeId) {
        nodeInfoService.removeById(nodeId);
        return ResultUtil.success();
    }

}

