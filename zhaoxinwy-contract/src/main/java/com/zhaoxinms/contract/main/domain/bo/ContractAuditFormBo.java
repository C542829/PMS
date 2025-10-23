package com.zhaoxinms.contract.main.domain.bo;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhaoxinms.common.core.domain.BaseEntity;
import com.zhaoxinms.common.core.validate.AddGroup;
import com.zhaoxinms.common.core.validate.EditGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同审批单业务对象 contract_audit_form
 *
 * @author fanhuibin
 * @date 2022-08-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("合同审批单业务对象")
public class ContractAuditFormBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 合同id
     */
    @ApiModelProperty(value = "合同id", required = true)
    @NotNull(message = "合同id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long contractId;
    
    /**
     * 合同名
     */
    @ApiModelProperty(value = "合同名", required = true)
    @NotBlank(message = "合同名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contractName;

    /**
     * 合同主体
     */
    @ApiModelProperty(value = "合同主体", required = true)
    @NotBlank(message = "合同主体不能为空", groups = { AddGroup.class, EditGroup.class })
    private String subjects;

    /**
     * 合同合作方
     */
    @ApiModelProperty(value = "合同合作方", required = true)
    @NotBlank(message = "合同合作方不能为空", groups = { AddGroup.class, EditGroup.class })
    private String partners;

    /**
     * 币种
     */
    @ApiModelProperty(value = "币种", required = true)
    @NotBlank(message = "币种不能为空", groups = { AddGroup.class, EditGroup.class })
    private String currency;

    /**
     * 合同金额
     */
    @ApiModelProperty(value = "合同金额", required = true)
    @NotNull(message = "合同金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String amount;

    /**
     * 收支类型
     */
    @ApiModelProperty(value = "收支类型", required = true)
    @NotBlank(message = "收支类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inouttype;

    /**
     * 所属项目
     */
    @ApiModelProperty(value = "所属项目", required = true)
    private Long projectId;

    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间", required = true)
    @NotNull(message = "生效时间不能为空", groups = { AddGroup.class, EditGroup.class })
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date effectiveTime;

    /**
     * 截止时间
     */
    @ApiModelProperty(value = "截止时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 盖章顺序
     */
    @ApiModelProperty(value = "盖章顺序", required = true)
    @NotBlank(message = "盖章顺序不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stampOrder;

    /**
     * 印章类型
     */
    @ApiModelProperty(value = "印章类型", required = true)
    @NotBlank(message = "印章类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stampType;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = true)
    private String remarks;

    /**
     * 扩展表单配置
     */
    @ApiModelProperty(value = "扩展表单配置", required = true)
    private String extForm;

    /**
     * 扩展表单字段
     */
    @ApiModelProperty(value = "扩展表单字段", required = true)
    private String extData;

    /**
     * 申请人
     */
    @ApiModelProperty(value = "申请人", required = true)
    @NotBlank(message = "申请人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String applyUserId;

    /**
     * 申请部门
     */
    @ApiModelProperty(value = "申请部门", required = true)
    @NotBlank(message = "申请部门不能为空", groups = { AddGroup.class, EditGroup.class })
    private String applyDept;
    
    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private String uploadFile;
    
    private String instanceDefKey;
    
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
    

    /**
     * 资质类型
     */
    private String certificateType;

    private String isTemplate; //是否是模板合同
    private String projectName;  //合同项目名
    private String projectManager; //项目经理
    private String projectCode; //项目编号
    private String moneyPeriod; //账期
    private String renewal; //是否续约
    private String renewalText; //续约条款
}
