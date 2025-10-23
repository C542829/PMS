package com.zhaoxinms.contract.main.domain.bo;

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
 * 合同台账bo
 * @author huibi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("合同台账")
public class ContractBookBo extends BaseEntity {


    @ApiModelProperty(value = "创建时间范围", required = true)
    private List<Long> createTimeRange;
    
    @ApiModelProperty(value = "生效时间范围", required = true)
    private List<Long> effectiveTimeRange;

    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称", required = true)
    private String contractName;
    
    /**
     * 合同类型
     */
    @ApiModelProperty(value = "合同类型", required = true)
    private List<String> contractTypeArray;

    /**
     * 合同类型
     */
    @ApiModelProperty(value = "合同类型", required = true)
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
     * 合同编号
     */
    private List<Long> contractStatus;

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
     * 合同编号
     */
    private String contractCode;
    
    /**
     * 合同备注
     */
    private String remarks;
    

    @ApiModelProperty(value = "所属档案名称", required = false)
    private String archivesName;

    @ApiModelProperty(value = "所属档案id", required = false)
    private String archivesId;
    
    @ApiModelProperty(value = "签约主体", required = false)
    private String subject;
    
    @ApiModelProperty(value = "合作方", required = false)
    private String partner;
    
    @ApiModelProperty(value = "收支类型", required = false)
    private String inouttype;
    
    /**
     * 创建时间排序
     */
    private String createTimeOrder;
    
    /**
     * 生效时间排序
     */
    private String effectiveTimeOrder;
    
    /**
     * 审批通过时间排序
     */
    private String contractApprovePassTimeOrder;
    /**
     * 截止时间排序
     */
    private String endTimeOrder;
    /**
     * 合同负责人
     */
    private String contractChargeName;
}
