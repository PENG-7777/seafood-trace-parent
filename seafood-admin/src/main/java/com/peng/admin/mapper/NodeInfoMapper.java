package com.peng.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.admin.entity.NodeInfo;
import com.peng.admin.vo.NodeVO;
import org.apache.ibatis.annotations.Param;

/**
 * 节点企业Mapper持久层
 * 继承MyBatis-Plus BaseMapper，内置所有单表CRUD能力
 */
public interface NodeInfoMapper extends BaseMapper<NodeInfo> {

    /**
     * 分页查询企业列表（关联省份、城市表，返回带中文名称的VO）
     * @param page MP分页对象
     * @param name 企业名称模糊查询条件
     * @param type 企业类型筛选条件
     * @param provId 省份编号筛选条件
     * @param status 企业注册状态筛选条件
     * @return 分页VO结果
     */
    IPage<NodeVO> selectNodePage(Page<NodeVO> page,
                                 @Param("name") String name,
                                 @Param("type") Integer type,
                                 @Param("provId") Integer provId,
                                 @Param("status") Integer status);

    /**
     * 根据ID查询企业详情（关联省份、城市表）
     * @param nodeId 企业编号
     * @return 企业详情VO
     */
    NodeVO selectNodeDetailById(@Param("nodeId") Integer nodeId);
}
