package com.peng.node.mapper;

import com.peng.node.entity.Province;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 省份字典Mapper
 * 负责省份字典数据数据库查询操作
 */
@Mapper
public interface ProvinceMapper {

    /**
     * 查询全部省份列表
     * @return 所有省份集合
     */
    List<Province> selectAll();
}
