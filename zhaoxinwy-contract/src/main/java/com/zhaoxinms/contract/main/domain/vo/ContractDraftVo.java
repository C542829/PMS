package com.zhaoxinms.contract.main.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zhaoxinms.common.core.validate.AddGroup;
import com.zhaoxinms.common.core.validate.EditGroup;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.contract.main.domain.ContractAuditForm;
import com.zhaoxinms.contract.main.enums.ContractEnums;
import com.zhaoxinms.contract.main.service.impl.ContractDraftServiceImpl;
import com.zhaoxinms.contract.type.domain.vo.FormVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 合同草拟视图对象 contract_draft
 *
 * @author ruoyi
 * @date 2022-08-17
 */
@Data
@ApiModel("合同草拟视图对象")
@ExcelIgnoreUnannotated
public class ContractDraftVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private String id;

    /**
     * 合同名称
     */
    @ExcelProperty(value = "合同名称")
    @ApiModelProperty("合同名称")
    private String contractName;

    /**
     * 合同类型
     */
    @ExcelProperty(value = "合同类型")
    @ApiModelProperty("合同类型")
    private String contractType;

    /**
     * 合同类型名称
     */
    @ApiModelProperty(value = "合同类型名称", required = true)
    @NotBlank(message = "合同类型名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contractTypeName;

    /**
     * 合同类型明细
     */
    private FormVo form;

    /**
     * 文件组版本名称
     */
    @ExcelProperty(value = "文件组版本名称")
    @ApiModelProperty("文件组版本名称")
    private String fileGroupsVersionName;

    /**
     * 协同状态
     */
    @ExcelProperty(value = "协同状态")
    @ApiModelProperty("协同状态")
    private String coordinationStatus;

    /**
     * 协同人
     */
    @ExcelProperty(value = "协同人")
    @ApiModelProperty("协同人")
    private String contractCollaborators;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

    private String fileGroup;

    private String fileGroupsVersion;
    
    /**
     * 文件组id
     */
    private String fileGroupsId;

    /**
     * 合同状态
     */
    private String contractStatus;
    /**
     * 印章状态
     */
    private String contractStampedStatus;
    /**
     * 归档状态
     */
    private String contractFilingStatus;
    /**
     *  归档时间
     */
    private Date contractFilingTime;
    /**
     * 复核状态
     */
    private String contractReviewStatus;
    /**
     * 变更状态
     */
    private String contractChangeStatus;

    /**
     * 审批状态
     */
    private String contractApproveStatus;
    /**
     *  审批申请时间
     */
    private Date contractApproveApplyTime;
    /**
     *  审批通过时间
     */
    private Date contractApprovePassTime;
    /**
     * 纠纷状态
     */
    private String contractDisputeStatus;
    /**
     * 纠纷登记人名称
     */
    private String disputeRegistrantName;

    /**
     * 纠纷登记人id
     */
    private String disputesRegistrantId;
    /**
     * 纠纷原因
     */
    private String contractDisputesReason;
    /**
     *  纠纷登记时间
     */
    private Date disputesRegisterTime;
    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 协同人名称展示
     */
    private String contractCollaboratorsName;

    /**
     * 归档-档案ID
     */
    private String contractArchivesId;
    /**
     * 档案名称
     */
    private String archivesName;
    /**
     * 档案密级
     */
    private String archivesClassified;
    /**
     * 归档时间
     */
    private Date contractArchivesTime;

    /**
     * 所属项目id
     */
    private String projectId;
    /**
     * 所属项目
     */
    private String projectName;
    /**
     * 所属项目编号
     */
    private String projectCode;
    /**
     * 拟稿人
     */
    private String drafterName;
    /**
     * 主合同正文是否归档 0 未归档 1归档
     */
    private String mainFileToArchive  = "0";
    /**
     * 是否包含未归档文件 0 没有  1有
     */
    private String noArchive = "0";
    /**
     * 是否包含合同阅读权限
     */
    private String hasBorrowP = "0";

    /**
     * 合同负责人id
     */
    private String contractChargeId;

    /**
     * 合同负责人名称
     */
    private String contractChargeName;

    /**
     * 合同申请表
     */
    private ContractAuditForm auditForm;
    
    /**
     * 合同主体
     */
    private String subjects;

    /**
     * 合同合作方
     */
    private String partners;
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
    
    private String subjectNames;
    private String partnerNames;
    /**
     * 币种
     */
    private String currency;
    /**
     * 合同金额
     */
    private String amount;
    /**
     * 收支类型
     */
    private String inouttype;
    /**
     * 生效时间
     */
    private Date effectiveTime;
    /**
     * 截止时间
     */
    private Date endTime;
    /**
     * 扩展表单配置
     */
    private String extForm;
    /**
     * 扩展表单字段
     */
    private String extData;
    
    private Long changeId;
    
    private Long auditId;
    /**
     * 合同身份 拟稿和负责
     */
    private String contractRole;
    /**
     * 拟稿人id
     */
    private String drafterId;
    
    private Long sealFilesId;
    
    private int undoTaskNum;
    
    private int doneTaskNum;
    
    private BigDecimal excludeTaxAmount; //金额（不含税）
    private String tax; //税率
    private BigDecimal taxAmount; //增值税税额
    private String invoiceType; //发票类型
    private String isTemplate; //是否是模板合同
    private String projectManager; //项目经理
    private String moneyPeriod; //账期
    private String certificateType;//公司资质
    private String renewal; //是否续约
    private String renewalText; //续约条款
    private String remarks;
    
    
    private String fulfilStatus;
    
    /**
     * 合同作废 0 未作废 1 已作废
     */
    private String contractCancel;
    /**
     * 合同作废原因
     */
    private String contractCancelRemark;
}
