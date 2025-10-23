package com.zhaoxinms.contract.conf.domain.vo;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 签约主体视图对象 t_signing_subject
 *
 * @author ruoyi
 * @date 2022-07-12
 */
@Data
@ApiModel("签约主体视图对象")
@ExcelIgnoreUnannotated
public class TSigningSubjectVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private String id;

    /**
     * 签约主体编号
     */
    @ExcelProperty(value = "签约主体编号")
    @ApiModelProperty("签约主体编号")
    private String signingSubjectCode;

    /**
     * 签约主体名称
     */
    @ExcelProperty(value = "签约主体名称")
    @ApiModelProperty("签约主体名称")
    private String signingSubjectName;

    /**
     * 企业经营状态
     */
    @ApiModelProperty("企业经营状态")
    private String businessStatus;

    /**
     * 统一社会信用代码
     */
    @ExcelProperty(value = "统一社会信用代码")
    @ApiModelProperty("统一社会信用代码")
    private String uscc;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ApiModelProperty("备注")
    private String remarks;
    
    @ExcelProperty(value = "可用状态")
    @ApiModelProperty(value = "可用状态")
    private String status;

    private Boolean disabled;
    
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    
    @ApiModelProperty(value = "法人姓名")
    private String legalPersonName;
    
    @ApiModelProperty(value = "法人身份证号")
    private String legalPersonIdCard;
}
