package com.zhaoxinms.contract.conf.domain.bo;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhaoxinms.common.core.domain.BaseEntity;
import com.zhaoxinms.common.core.validate.AddGroup;
import com.zhaoxinms.common.core.validate.EditGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合作方业务对象 t_contract_counterpart
 *
 * @author ruoyi
 * @date 2022-07-18
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("合作方业务对象")
public class TContractCounterpartBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 合作方名称
     */
    @ApiModelProperty(value = "合作方名称", required = true)
    @NotBlank(message = "合作方名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contractCounterpartName;

//    @ApiModelProperty(value = "合作方编号", required = true)
//    @NotBlank(message = "合作方编号不能为空", groups = { AddGroup.class, EditGroup.class })
//    private String contractCounterpartCode;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty(value = "统一社会信用代码", required = true)
    @NotBlank(message = "统一社会信用代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String uscc;

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "企业类型")
    private String companyType;


    /**
     * 注册资本
     */
    @ApiModelProperty(value = "注册资本")
    private String registeredCapital;


    /**
     * 注册地址
     */
    @ApiModelProperty(value = "注册地址")
    private String address;

    /**
     * 法人姓名
     */
    @ApiModelProperty(value = "法人姓名")
    private String legalPersonName;

    /**
     * 法人身份证号
     */
    @ApiModelProperty(value = "法人身份证号")
    private String legalPersonIdCard;

    /**
     * 经营状态
     */
    @ApiModelProperty(value = "经营状态")
    private String businessStatus;

    /**
     * 营业期限开始时间
     */
    @ApiModelProperty(value = "营业期限开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessStartDate;

    /**
     * 营业期限结束时间
     */
    @ApiModelProperty(value = "营业期限结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessEndDate;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    @Size(min = 0,max = 4000,message = "联系人信息过多，请不要填写过多的联系人")
    private String contacts;

    /**
     * 资质信息
     */
    @ApiModelProperty(value = "资质信息")
    @Length(max = 4000,message = "资质信息信息过多，请不要填写过多的资质信息")
    private String attachments;

    @ApiModelProperty(value = "银行信息")
    @Length(max = 4000,message = "银行信息信息过多，请不要填写过多的银行信息")
    private String bankJson;

    @ApiModelProperty(value = "长期")
    private Integer forever;

    @ApiModelProperty(value = "地图名称")
    private String areaName;

    @ApiModelProperty(value = "地区编号")
    private String areaCode;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "可用状态")
    private String status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    
    /**
     * 合作方负责人
     */
    @ApiModelProperty(value = "负责人")
    private String contractCounterpartHead;
    
    /**
     * 营业范围
     */
    @ApiModelProperty(value = "营业范围")
    private String businessScope;


}
