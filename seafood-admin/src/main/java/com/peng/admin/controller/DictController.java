package com.peng.admin.controller;


import com.peng.admin.common.Result;
import com.peng.admin.entity.City;
import com.peng.admin.entity.Province;
import com.peng.admin.service.CityService;
import com.peng.admin.service.ProvinceService;
import com.peng.admin.vo.DictVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/dict")
public class DictController {

    @Resource
    private ProvinceService provinceService;
    @Resource
    private CityService cityService;

    /**
     * 获取全部省份列表
     */
    @GetMapping("/province")
    public Result<List<Province>> getProvinceList(){
        return Result.ok(provinceService.list());
    }

    /**
     * 根据provId获取对应城市
     */
    @GetMapping("/city/{provId}")
    public Result<List<City>> getCityByProvId(@PathVariable Integer provId){
        List<City> cityList = cityService.lambdaQuery()
                .eq(City::getProvId, provId)
                .list();
        return Result.ok(cityList);
    }

    /**
     * 企业类型字典（固定枚举）
     */
    @GetMapping("/nodeType")
    public Result<List<DictVO>> getNodeType(){
        List<DictVO> list = Arrays.asList(
                new DictVO(1,"捕捞企业"),
                new DictVO(2,"养殖企业"),
                new DictVO(3,"加工企业"),
                new DictVO(4,"批发商"),
                new DictVO(5,"零售企业")
        );
        return Result.ok(list);
    }
}
