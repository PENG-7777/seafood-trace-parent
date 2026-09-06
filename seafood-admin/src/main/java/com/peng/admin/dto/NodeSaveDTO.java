package com.peng.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 企业节点新增/保存DTO
 * 用于管理员新增、编辑流通企业信息（捕捞/养殖/批发/零售）
 */
@Data
public class NodeSaveDTO {

    /**
     * 节点企业主键ID
     * 新增时不传；编辑修改时必须传递该值
     */
    private Integer nodeId;

    /**
     * 流通端登录账号编码（唯一）
     */
    @NotBlank(message = "企业登录编码不能为空")
    private String code;

    /**
     * 登录密码，新增时必填，编辑不传则不修改密码
     */
    private String password;

    /**
     * 节点企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    private String name;

    /**
     * 企业类型：1捕捞企业,2养殖企业,3批发商,4零售商
     */
    @NotNull(message = "企业类型不能为空")
    private Integer type;

    /**
     * 所属省份编号
     */
    @NotNull(message = "所属省份不能为空")
    private Integer provId;

    /**
     * 所属城市编号
     */
    @NotNull(message = "所属城市不能为空")
    private Integer cityId;

    /**
     * 企业详细地址
     */
    @NotBlank(message = "企业详细地址不能为空")
    private String address;

    /**
     * 营业执照编号
     */
    @NotBlank(message = "营业执照编号不能为空")
    private String businessId;

    /**
     * 渔业捕捞许可证编号（type=1捕捞企业必填）
     */
    private String fishingLic;

    /**
     * 水域滩涂养殖证编号（type=2养殖企业必填）
     */
    private String aquacultureLic;

    /**
     * 食品经营许可证编号（type=3批发 / type=4零售必填）
     */
    private String foodBusinessLic;

    /**
     * 法定代表人
     */
    @NotBlank(message = "法定代表人不能为空")
    private String corporation;

    /**
     * 企业联系电话
     */
    @NotBlank(message = "企业联系电话不能为空")
    private String telephone;

    /**
     * 企业注册日期
     */
    @NotNull(message = "企业注册日期不能为空")
    private LocalDate regDate;

    /**
     * 备注信息
     */
    private String remarks;

}

