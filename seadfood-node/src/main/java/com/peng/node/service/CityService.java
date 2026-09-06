package com.peng.node.service;

import com.peng.node.entity.City;
import java.util.List;

/**
 * 城市字典业务接口
 */
public interface CityService {

    /**
     * 根据省份编号获取城市列表
     * @param provId 省份ID
     * @return 当前省份下全部城市
     */
    List<City> getCityListByProvId(Integer provId);
}
