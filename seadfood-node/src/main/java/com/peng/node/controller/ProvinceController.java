package com.peng.node.controller;

import com.peng.node.entity.Province;
import com.peng.node.service.ProvinceService;
import com.peng.node.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 省份字典控制器
 * 提供前端省市级联下拉的省份数据接口
 */
@RestController
@RequestMapping("/api/node/province")
public class ProvinceController {

    @Resource
    private ProvinceService provinceService;

    /**
     * 获取全部省份列表
     * @return Result包装的省份集合
     */
    @GetMapping("/list")
    public Result<List<Province>> getProvinceList() {
        List<Province> provinceList = provinceService.getAllProvinceList();
        return Result.ok(provinceList);
    }
}
