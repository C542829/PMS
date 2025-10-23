package com.zhaoxinms.contract.main.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhaoxinms.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同审批单对象 contract_audit_form
 *
 * @author fanhuibin
 * @date 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("contract_audit_form")
public class ContractAuditForm extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    
    /**
     * 审批单号
     */
    private String auditCode;
    /**
     * 审批状态
     */
    private String auditStatus;
    /**
     * 合同id
     */
    private Long contractId;
    /**
     * 合同名
     */
    private String contractName;
    /**
     * 合同主体
     */
    private String subjects;
    /**
     * 合同主体
     */
    private String subjectNames;
    /**
     * 合同合作方
     */
    private String partners;
    /**
     * 合同合作方
     */
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
     * 所属项目
     */
    private Long projectId;
    /**
     * 生效时间
     */
    private Date effectiveTime;
    /**
     * 截止时间
     */
    private Date endTime;
    /**
     * 盖章顺序
     */
    private String stampOrder;
    /**
     * 印章类型
     */
    private String stampType;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 扩展表单配置
     */
    private String extForm;
    /**
     * 扩展表单字段
     */
    private String extData;
    /**
     * 申请人
     */
    private String applyUserId;
    private String applyUserName;
    /**
     * 申请部门
     */
    private String applyDept;
    private String applyDeptName;
    /**
     * 0 未删除 1 已删除
     */
    @TableLogic
    private String delFlag;
    /**
     * 租户id
     */
    private String tenantId;
    private String uploadFile;
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
    private BigDecimal excludeTaxAmount; //金额（不含税）
    private String tax; //税率
    private BigDecimal taxAmount; //增值税税额
    private String invoiceType; //发票类型
    
    private String currentTask;
    private String currentAssignee;
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

