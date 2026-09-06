package com.peng.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peng.admin.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员Mapper持久层接口
 * 继承BaseMapper获得MyBatis‑Plus内置CRUD方法
 * 对应数据库表：admin
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

}
