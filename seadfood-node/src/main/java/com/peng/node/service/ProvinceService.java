package com.peng.node.service;

import com.peng.node.entity.Province;
import java.util.List;

/**
 * 省份字典业务接口
 */
public interface ProvinceService {

    /**
     * 获取全部省份数据列表
     * @return 省份集合
     */
    List<Province> getAllProvinceList();
}
