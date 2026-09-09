package com.peng.node.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peng.node.entity.RetaBatch;
import com.peng.node.vo.RetailApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 零售商批号Mapper
 * MyBatis‑Plus BaseMapper提供单表CRUD操作
 */
@Mapper
public interface RetaBatchMapper extends BaseMapper<RetaBatch> {
    /**
     * 多表联查：获取提交给指定批发商的零售商进场申请列表
     * @param wholNodeId 当前登录批发商企业ID
     * @return RetailApplyVO集合
     */
    List<RetailApplyVO> selectRetaApplyListByWholNodeId(@Param("wholNodeId") Integer wholNodeId);
}
