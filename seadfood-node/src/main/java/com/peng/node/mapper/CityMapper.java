package com.peng.node.mapper;

import com.peng.node.entity.City;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 城市字典Mapper
 * 负责城市数据数据库查询操作
 */
@Mapper
public interface CityMapper {

    /**
     * 根据省份id查询该省份下所有城市列表
     * @param provId 省份编号
     * @return 城市集合
     */
    List<City> selectByProvId(Integer provId);
}
