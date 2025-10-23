package com.zhaoxinms.contract.main.domain.bo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.zhaoxinms.common.core.domain.BaseEntity;
import com.zhaoxinms.common.core.validate.AddGroup;
import com.zhaoxinms.common.core.validate.EditGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同草拟业务对象 contract_draft
 *
 * @author ruoyi
 * @date 2022-08-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("合同草拟业务对象")
public class ContractDraftBo extends BaseEntity {
	
	/**
	 * 存量合同标识
	 */
	public static final String IS_STOCK = "1";

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    @ApiModelProperty(value = "发起审批时间范围", required = true)
    private List<Long> createTimeRange;

    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称", required = true)
    @NotBlank(message = "合同名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contractName;

    /**
     * 合同类型
     */
    @ApiModelProperty(value = "合同类型", required = true)
    @NotBlank(message = "合同类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contractType;

    /**
     * 合同类型名称
     */
    @ApiModelProperty(value = "合同类型名称", required = true)
    @NotBlank(message = "合同类型名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contractTypeName;

    /**
     * 关联合同id
     */
    @ApiModelProperty(value = "关联合同id", required = true)
    private String relatedContractId;

    /**
     * 关联合同名称
     */
    @ApiModelProperty(value = "关联合同名称", required = true)
    private String relatedContractName;

    /**
     * 关联合同编号
     */
    @ApiModelProperty(value = "关联合同编号", required = true)
    private String relatedContractCode;

    /**
     * 关联类型
     */
    @ApiModelProperty(value = "关联类型", required = true)
    private String relatedType;

    /**
     * 关联时间
     */
    @ApiModelProperty(value = "关联时间", required = true)
    private Date relatedTime;

    /**
     * 合同模板id
     */
    @ApiModelProperty(value = "合同模板id", required = true)
    private String contractTemplateId;

    /**
     * 合同模板类型
     */
    @ApiModelProperty(value = "合同模板类型", required = true)
    private String contractTemplateType;

    /**
     * 拟稿人
     */
    @ApiModelProperty(value = "拟稿人", required = true)
    private String drafterName;

    /**
     * 拟稿人id
     */
    @ApiModelProperty(value = "拟稿人id", required = true)
    private String drafterId;

    /**
     * 文件组版本
     */
    @ApiModelProperty(value = "文件组版本", required = true)
    private String fileGroupsVersion;

    /**
     * 文件组版本名称
     */
    @ApiModelProperty(value = "文件组版本名称", required = true)
    private String fileGroupsVersionName;

    /**
     * 当前审批人
     */
    @ApiModelProperty(value = "当前审批人", required = true)
    private String currentReviewer;

    /**
     * 当前审批人id
     */
    @ApiModelProperty(value = "当前审批人id", required = true)
    private String currentReviewerId;

    /**
     * 所属项目id
     */
    @ApiModelProperty(value = "所属项目id", required = true)
    private String projectId;

    /**
     * 所属项目
     */
    @ApiModelProperty(value = "所属项目", required = true)
    private String projectName;

    /**
     * 所属项目编号
     */
    @ApiModelProperty(value = "所属项目编号", required = true)
    private String projectCode;

    /**
     * 文件组id
     */
    @ApiModelProperty(value = "文件组id", required = true)
    private String fileGroupsId;

    /**
     * 协同状态
     */
    @ApiModelProperty(value = "协同状态", required = true)
    private String coordinationStatus;

    /**
     * 协同人
     */
    @ApiModelProperty(value = "协同人", required = true)
    private String contractCollaborators;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = true)
    private String remarks;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id", required = true)
    private String tenantId;

    /**
     * 文件类型
     */
    private String fileGroup;

    /**
     * 合同编号
     */
    private String contractCode;
    
    /**
     * 合同状态
     */
    private String contractStatus;

    /**
     * 纠纷原因
     */
    @ApiModelProperty(value = "纠纷原因", required = true)
    private String contractDisputesReason;

    @ApiModelProperty(value = "纠纷状态", required = true)
    private String contractDisputeStatus;

    @ApiModelProperty(value = "所属档案名称", required = true)
    private String archivesName;

    @ApiModelProperty(value = "所属档案id", required = true)
    private String archivesId;
    
    private BigDecimal excludeTaxAmount; //金额（不含税）
    private String tax; //税率
    private BigDecimal taxAmount; //增值税税额
    private String invoiceType; //发票类型
    /**
     * 合同作废 0 未作废 1 已作废
     */
    private String contractCancel;
    /**
     * 合同作废原因
     */
    private String contractCancelRemark;

    private String isTemplate; //是否是模板合同
    private String projectManager; //项目经理
    private String moneyPeriod; //账期
    private String certificateType;//公司资质
    private String renewal; //是否续约
    private String renewalText; //续约条款
    
    private String filter;
    private String createTimeOrder;
    private String effectiveTimeOrder;
    private String partnerNames;
    private String inouttype;
}
