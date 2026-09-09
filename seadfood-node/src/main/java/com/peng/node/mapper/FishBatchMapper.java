package com.peng.node.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peng.node.entity.FishBatch;
import com.peng.node.vo.ProcessApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 捕捞企业批号Mapper
 * MyBatis‑Plus BaseMapper提供单表CRUD
 */
@Mapper
public interface FishBatchMapper extends BaseMapper<FishBatch> {

    List<ProcessApplyVO> selectApplyListByNodeId(@Param("nodeId") Integer nodeId);


}
