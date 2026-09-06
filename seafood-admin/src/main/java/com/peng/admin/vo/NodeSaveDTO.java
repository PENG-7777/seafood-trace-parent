package com.peng.admin.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class NodeSaveDTO {
    private Integer nodeId;

    @NotBlank(message = "流通端登录编码不能为空")
    private String code;

    @NotBlank(message = "企业名称不能为空")
    private String name;

    @NotNull(message = "企业类型不能为空")
    private Integer type;

    @NotNull(message = "省份编号不能为空")
    private Integer provId;

    @NotNull(message = "城市编号不能为空")
    private Integer cityId;

    private String address;
    private String businessId;
    private String fishingLic;
    private String aquacultureLic;
    private String foodBusinessLic;

    @NotBlank(message = "法定代表人不能为空")
    private String corporation;

    @NotBlank(message = "联系电话不能为空")
    private String telephone;

    private LocalDate regDate;
    private String remarks;
}
