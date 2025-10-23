package com.zhaoxinms.contract.main.domain.bo;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.zhaoxinms.common.core.domain.BaseEntity;
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
@ApiModel("合同草拟状态对象")
public class ContractStatusBo extends BaseEntity {
	
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

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

    
    private String fileGroup;
    
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


}
