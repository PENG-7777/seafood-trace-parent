package com.peng.node.controller;

import com.peng.node.entity.City;
import com.peng.node.service.CityService;
import com.peng.node.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 城市字典控制器
 * 提供省市级联下拉接口
 */
@RestController
@RequestMapping("/api/node/city")
public class CityController {

    @Resource
    private CityService cityService;

    /**
     * 根据省份id查询城市列表
     * @param provId 省份编号
     * @return Result包装的城市集合
     */
    @GetMapping("/list/{provId}")
    public Result<List<City>> getCityList(@PathVariable("provId") Integer provId) {
        List<City> cityList = cityService.getCityListByProvId(provId);
        return Result.ok(cityList);
    }
}
