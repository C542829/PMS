package com.zhaoxinms.contract.main.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhaoxinms.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同草拟对象 contract_draft
 *
 * @author ruoyi
 * @date 2022-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("contract_draft")
public class ContractDraft extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 合同类型名称
     */
    private String contractTypeName;
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
    /**
     * 合同模板id
     */
    private String contractTemplateId;
    /**
     * 合同模板类型
     */
    private String contractTemplateType;
    /**
     * 拟稿人
     */
    private String drafterName;
    /**
     * 拟稿人id
     */
    private String drafterId;
    /**
     * 文件组版本
     */
    private String fileGroupsVersion;
    /**
     * 文件组版本名称
     */
    private String fileGroupsVersionName;
    /**
     * 当前审批人
     */
    private String currentReviewer;
    /**
     * 当前审批人id
     */
    private String currentReviewerId;
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
     * 文件组id
     */
    private String fileGroupsId;
    /**
     * 协同状态
     */
    private String coordinationStatus;
    /**
     * 协同人
     */
    private String contractCollaborators;
    /**
     * 0 未删除 1 已删除
     */
    @TableLogic
    private String delFlag;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 合同状态
     */
    private String contractStatus;
    /**
     * 印章状态
     */
    private String contractStampedStatus;
    /**
     * 用印完成时间
     */
    private Date contractStampedTime;
    /**
     * 归档状态
     */
//    private String contractFilingStatus;
    /**
     *  归档时间
     */
//    private Date contractFilingTime;
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
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.VARCHAR)
    private String disputeRegistrantName;

    /**
     * 纠纷登记人id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.VARCHAR)
    private String disputesRegistrantId;
    /**
     * 纠纷原因
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.VARCHAR)
    private String contractDisputesReason;
    /**
     *  纠纷登记时间
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.TIMESTAMP)
    private Date disputesRegisterTime;

    /**
     * 所属档案的ID，需要表关联
     */
    private String contractArchivesId;
    /**
     *  归档时间-最后一次选择时间
     */
    private Date contractArchivesTime;
    /**
     * 最后一次归档更新人ID
     */
    private Long contractArchivesUserId;
    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 协同人名称展示
     */
    private String contractCollaboratorsName;
    
    /**
     * 合同负责人id
     */
    private String contractChargeId;
    
    /**
     * 合同负责人名称
     */
    private String contractChargeName;
    
    /**
     * 合同生效时间
     */
    private Date effectiveTime;
    
    /**
     * 合同结束时间
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date endTime;

    /**
     * 合同主体
     */
    private String subjects;
    
    /**
     * 合同合作方
     */
    private String partners;
    
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
     * 扩展表单配置
     */
    private String extForm;
    /**
     * 扩展表单字段
     */
    private String extData;
    
    //变更单id
    private Long changeId;
    
    //审批单id
    private Long auditId;
    
    private Long sealFilesId;
    
    private BigDecimal excludeTaxAmount; //金额（不含税）
    private String tax; //税率
    private BigDecimal taxAmount; //增值税税额
    private String invoiceType; //发票类型
    
    /**
     * 存量合同标识 0 否 1 是
     */
    private String contractStock;
    /**
     * 合同作废 0 未作废 1 已作废
     */
    private String contractCancel;
    /**
     * 合同作废原因
     */
    private String contractCancelRemark;
    
    /**
     * 资质类型
     */
    private String certificateType;
    
    
    private String isTemplate; //是否是模板合同
    private String projectManager; //项目经理
    private String moneyPeriod; //账期
    private String renewal; //是否续约
    private String renewalText; //续约条款
}
