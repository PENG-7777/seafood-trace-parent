package com.peng.node.service.impl;

import com.peng.node.entity.Province;
import com.peng.node.mapper.ProvinceMapper;
import com.peng.node.service.ProvinceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 省份字典业务实现类
 */
@Service
public class ProvinceServiceImpl implements ProvinceService {

    @Resource
    private ProvinceMapper provinceMapper;

    /**
     * 查询全部省份列表
     * @return 省份集合
     */
    @Override
    public List<Province> getAllProvinceList() {
        return provinceMapper.selectAll();
    }
}
