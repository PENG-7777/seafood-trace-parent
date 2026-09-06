package com.peng.node.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("city")
public class City {
    @TableId(type = IdType.AUTO)
    private Integer cityId;
    private Integer provId;
    private String cityName;
}
