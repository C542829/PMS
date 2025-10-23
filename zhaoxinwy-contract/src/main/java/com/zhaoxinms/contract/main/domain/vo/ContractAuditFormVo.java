package com.zhaoxinms.contract.main.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 合同审批单视图对象 contract_audit_form
 *
 * @author fanhuibin
 * @date 2022-08-29
 */
@Data
@ApiModel("合同审批单视图对象")
@ExcelIgnoreUnannotated
public class ContractAuditFormVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;
    
    /**
     * 审批单号
     */
    @ExcelProperty(value = "审批单号")
    @ApiModelProperty("审批单号")
    private String auditCode;
    
    /**
     * 审批状态
     */
    @ExcelProperty(value = "审批状态")
    @ApiModelProperty("审批状态")
    private String auditStatus;

    /**
     * 合同id
     */
    @ExcelProperty(value = "合同id")
    @ApiModelProperty("合同id")
    private Long contractId;

    /**
     * 合同名
     */
    @ExcelProperty(value = "合同名")
    @ApiModelProperty("合同名")
    private String contractName;

    /**
     * 合同主体
     */
    @ExcelProperty(value = "合同主体")
    @ApiModelProperty("合同主体")
    private String subjects;

    /**
     * 合同合作方
     */
    @ExcelProperty(value = "合同合作方")
    @ApiModelProperty("合同合作方")
    private String partners;

    /**
     * 币种
     */
    @ApiModelProperty("币种")
    private String currency;

    /**
     * 合同金额
     */
    @ExcelProperty(value = "合同金额")
    @ApiModelProperty("合同金额")
    private String amount;

    /**
     * 收支类型
     */
    @ApiModelProperty("收支类型")
    private String inouttype;

    /**
     * 所属项目
     */
    @ExcelProperty(value = "所属项目")
    @ApiModelProperty("所属项目")
    private Long projectId;

    /**
     * 生效时间
     */
    @ExcelProperty(value = "生效时间")
    @ApiModelProperty("生效时间")
    private Date effectiveTime;

    /**
     * 截止时间
     */
    @ExcelProperty(value = "截止时间")
    @ApiModelProperty("截止时间")
    private Date endTime;

    /**
     * 盖章顺序
     */
    @ApiModelProperty("盖章顺序")
    private String stampOrder;

    /**
     * 印章类型
     */
    @ApiModelProperty("印章类型")
    private String stampType;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ApiModelProperty("备注")
    private String remarks;

    /**
     * 扩展表单配置
     */
    @ExcelProperty(value = "扩展表单配置")
    @ApiModelProperty("扩展表单配置")
    private String extForm;

    /**
     * 扩展表单字段
     */
    @ExcelProperty(value = "扩展表单字段")
    @ApiModelProperty("扩展表单字段")
    private String extData;

    /**
     * 申请人
     */
    @ExcelProperty(value = "申请人")
    @ApiModelProperty("申请人")
    private String applyUserId;

    /**
     * 申请部门
     */
    @ExcelProperty(value = "申请部门")
    @ApiModelProperty("申请部门")
    private String applyDept;

    /**
     * 附件
     */
    @ApiModelProperty("审批附件")
    private String uploadFile;

    @ApiModelProperty("合作方")
    private String partnerNames;
    
    @ApiModelProperty("签约主体")
    private String subjectNames;
    
    private String instanceId; //流程id
    private String instanceDefKey; //流程定义
    
    /**
     * 关联合同id
     */
    private String relatedContractId;
    /**
     * 关联合同名称
     */
    private String relatedContractName;
    /**
     * 关联合同编号
     */
    private String relatedContractCode;
    /**
     * 关联类型
     */
    private String relatedType;
    /**
     * 关联时间
     */
    private Date relatedTime;
    private String invoiceType; //开票类型
    private BigDecimal excludeTaxAmount; //金额（不含税）
    private String tax; //税率
    private BigDecimal taxAmount; //增值税税额
    private Date createTime;
    
    private String isTemplate; //是否是模板合同
    private String projectName;  //合同项目名
    private String projectManager; //项目经理
    private String projectCode; //项目编号
    private String moneyPeriod; //账期
    private String certificateType;//公司资质
    private String renewal; //是否续约
    private String renewalText; //续约条款
}
