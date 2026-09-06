package com.peng.node.service.impl;

import com.peng.node.entity.City;
import com.peng.node.mapper.CityMapper;
import com.peng.node.service.CityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 城市字典业务实现
 */
@Service
public class CityServiceImpl implements CityService {

    @Resource
    private CityMapper cityMapper;

    /**
     * 根据省份编号查询城市列表
     * @param provId 省份ID
     * @return 城市集合
     */
    @Override
    public List<City> getCityListByProvId(Integer provId) {
        return cityMapper.selectByProvId(provId);
    }
}
