package com.peng.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("province")
public class Province {
    @TableId(type = IdType.AUTO)
    private Integer provId;
    private String provName;
}
