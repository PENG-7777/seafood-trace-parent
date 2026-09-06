package com.peng.node.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peng.node.entity.NodeInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 节点企业Mapper MyBatis‑Plus
 * BaseMapper提供：selectById、updateById、insert、deleteById等单表CRUD
 */
@Mapper
public interface NodeInfoMapper extends BaseMapper<NodeInfo> {

}
