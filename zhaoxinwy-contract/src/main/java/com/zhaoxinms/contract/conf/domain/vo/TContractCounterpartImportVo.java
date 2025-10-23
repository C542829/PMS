package com.zhaoxinms.contract.conf.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import io.swagger.annotations.ApiModel;
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
public class TContractCounterpartImportVo {

    private static final long serialVersionUID = 1L;


    /**
     * 合作方名称
     */
    @ExcelProperty(value = "合作方名称")
    private String contractCounterpartName;
//
//    @ExcelProperty(value = "合作方编号")
//    private String contractCounterpartCode;

    /**
     * 统一社会信用代码
     */
    @ExcelProperty(value = "统一社会信用代码")
    private String uscc;

    /**
     * 企业类型
     */
    private String companyType;

    /**
     * 注册资本
     */
    @ExcelProperty(value = "注册资本")
    private String registeredCapital;

    /**
     * 注册地址
     */
    @ExcelProperty(value = "注册地址")
    private String address;
    /**
     * 法人姓名
     */
    @ExcelProperty(value = "法人姓名")
    private String legalPersonName;
    /**
     * 法人身份证
     */
    @ExcelProperty(value = "法人身份证")
    private String legalPersonIdCard;
    
    private String businessStatus;
    /**
     * 营业期限开始时间
     */
    @ExcelProperty(value = "营业期限开始时间")
    private String businessStartDate;
    /**
     * 营业期限结束时间
     */
    @ExcelProperty(value = "营业期限结束时间")
    private String businessEndDate;
    
    @ExcelProperty(value = "是否永久：1长期，0非长期")
    private Integer forever;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remarks;

    /**
     * 电话
     */
    @ExcelProperty(value = "电话")
    private String tel;
    /**
     * 传真
     */
    @ExcelProperty(value = "传真")
    private String fax;
    /**
     * 地区编号
     */
    @ExcelProperty(value = "地区编号")
    private String areaCode;
    /**
     * 地区名
     */
    @ExcelProperty(value = "地区名")
    private String areaName;
    /**
     * 可用状态 0可用 1不可用
     */
    @ExcelProperty(value = "可用状态")
    private Integer status;
    
    /**
     * 营业范围
     */
    @ExcelProperty(value = "营业范围")
    private String businessScope;
}
