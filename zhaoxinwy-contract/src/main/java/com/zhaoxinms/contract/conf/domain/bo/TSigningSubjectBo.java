package com.zhaoxinms.contract.conf.domain.bo;

import javax.validation.constraints.NotBlank;

import com.zhaoxinms.common.core.domain.BaseEntity;
import com.zhaoxinms.common.core.validate.AddGroup;
import com.zhaoxinms.common.core.validate.EditGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 签约主体业务对象 t_signing_subject
 *
 * @author ruoyi
 * @date 2022-07-12
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("签约主体业务对象")
public class TSigningSubjectBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 签约主体编号
     */
    @ApiModelProperty(value = "签约主体编号", required = true)
    @NotBlank(message = "签约主体编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String signingSubjectCode;

    /**
     * 签约主体名称
     */
    @ApiModelProperty(value = "签约主体名称", required = true)
    @NotBlank(message = "签约主体名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String signingSubjectName;

    /**
     * 企业经营状态
     */
    @ApiModelProperty(value = "企业经营状态")
    private String businessStatus;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty(value = "统一社会信用代码", required = true)
    @NotBlank(message = "统一社会信用代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String uscc;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    
    @ApiModelProperty(value = "可用状态")
    private String status;
    
    @ApiModelProperty(value = "法人姓名")
    private String legalPersonName;
    
    @ApiModelProperty(value = "法人身份证号")
    private String legalPersonIdCard;


}
