package com.zhaoxinms.contract.conf.domain.vo;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 合作方视图对象 t_contract_counterpart
 *
 * @author ruoyi
 * @date 2022-07-18
 */
@Data
@ApiModel("合作方视图对象")
@ExcelIgnoreUnannotated
public class TContractCounterpartVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private String id;

    /**
     * 合作方名称
     */
    @ExcelProperty(value = "合作方名称")
    @ApiModelProperty("合作方名称")
    private String contractCounterpartName;

    @ExcelProperty(value = "合作方编号")
    @ApiModelProperty("合作方编号")
    private String contractCounterpartCode;

    /**
     * 统一社会信用代码
     */
    @ExcelProperty(value = "统一社会信用代码")
    @ApiModelProperty("统一社会信用代码")
    private String uscc;

    /**
     * 企业类型
     */
    @ApiModelProperty("企业类型")
    private String companyType;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contacts;

    /**
     * 资质信息
     */
    @ApiModelProperty(value = "资质信息")
    private String attachments;


    @ApiModelProperty(value = "银行信息")
    private String bankJson;

    /**
     * 注册资本
     */
    private String registeredCapital;

    /**
     * 注册地址
     */
    private String address;
    /**
     * 法人姓名
     */
    private String legalPersonName;
    /**
     * 法人身份证
     */
    private String legalPersonIdCard;
    /**
     * 经营状态
     */
    @ApiModelProperty("经营状态")
    private String businessStatus;
    /**
     * 营业期限开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessStartDate;
    /**
     * 营业期限结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessEndDate;
    /**
     * 备注
     */
    private String remarks;

    /**
     * 电话
     */
    private String tel;
    /**
     * 传真
     */
    private String fax;
    /**
     * 地区编号
     */
    private String areaCode;
    /**
     * 地区名
     */
    private String areaName;
    /**
       * 可用状态 0可用 1不可用
     */
    private Integer status;
    private Integer forever;

    /**
     *  合作次数
     */
    private Integer collTimes = 0;
    /**
     * 最后一次信用查询时间
     */
    private Date lastCredit;
    /**
     * 信用状态
     */
    private String creditStatus;
    
    /**
     * 合作方负责人
     */
    private String contractCounterpartHead;
    
    /**
     * 营业范围
     */
    private String businessScope;
    
    /**
     * 负责人名称
     */
    private String headName;
}
