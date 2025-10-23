package com.zhaoxinms.contract.conf.domain.vo;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * 合作方导出vo
 */
@Data
@NoArgsConstructor
public class ContractCounterpartExportVo {

    private static final long serialVersionUID = 1L;

    /**
     * 合作方名称
     */
    @ExcelProperty(value = "合作方名称")
    private String contractCounterpartName;
    
//    @ExcelProperty(value = "合作方编号")
//    private String contractCounterpartCode;
    
    /**
     * 申请类型 0 合同资质 1 变更资质
     */
    private String applyType;
    
    /**
     * 统一社会信用代码
     */
    @ExcelProperty(value = "统一社会信用代码")
    private String uscc;
    
    /**
     * 经营状态
     */
    private String businessStatus;
    
    /**
     * 法人姓名
     */
    private String legalPersonName;
    /**
     * 法人身份证
     */
    private String legalPersonIdCard;
    /**
     * 企业类型
     */
    private String companyType;
    /**
     * 合作方负责人
     */
    private String contractCounterpartHead;
    
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
     * 营业范围
     */
    private String businessScope;
    /**
     * 长期
     */
    private Integer forever;
    

}
