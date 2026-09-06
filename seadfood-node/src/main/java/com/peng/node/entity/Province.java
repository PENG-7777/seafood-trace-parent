package com.peng.node.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 省份字典实体类
 * 对应数据库表 province
 */
@Data
@TableName("province")
public class Province {
    /**
     * 省份编号，主键自增
     */
    @TableId(type = IdType.AUTO)
    private Integer provId;

    /**
     * 省份名称
     */
    private String provName;
}
