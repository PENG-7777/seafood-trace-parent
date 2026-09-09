package com.peng.node.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peng.node.entity.ProcessBatch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 冷冻加工企业批号Mapper
 * MyBatis‑Plus BaseMapper提供单表CRUD
 */
@Mapper
public interface ProcessBatchMapper extends BaseMapper<ProcessBatch> {
    /**
     * 查询待确认的加工申请列表，关联node_info查询加工企业名称
     * @param fsbIdList 当前养殖企业所有养殖批号id集合
     * @return ProcessBatch集合，填充nodeName字段
     */
    List<ProcessBatch> selectProcessApplyList(@Param("fsbIdList") List<Integer> fsbIdList);

}
