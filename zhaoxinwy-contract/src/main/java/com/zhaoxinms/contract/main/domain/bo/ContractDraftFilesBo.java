package com.zhaoxinms.contract.main.domain.bo;

import javax.validation.constraints.NotBlank;

import com.zhaoxinms.common.core.domain.BaseEntity;
import com.zhaoxinms.common.core.validate.EditGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同文件业务对象 contract_draft_files
 *
 * @author ruoyi
 * @date 2022-08-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("合同文件业务对象")
public class ContractDraftFilesBo extends BaseEntity {
	
	/**
	 * 创建合同时添加的合同文件
	 */
	public static final String FILE_TYPE_CONTRACT = "0";
	/**
	 * 发起审批时添加的附件
	 */
	public static final String FILE_TYPE_APPROVE = "1";
	/**
	 * 上传双章合同添加的附件
	 */
	public static final String FILE_TYPE_SEAL = "2";
	
	/**
	 * 合同变更上传双章合同添加的附件
	 */
	public static final String FILE_TYPE_CHANGE_SEAL = "3";
	
	/**
	 * 合同主文件
	 */
	public static final String IS_MAIN_FILE = "0";

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 草拟合同id
     */
    @ApiModelProperty(value = "草拟合同id", required = true)
    private String contractDraftId;

    /**
     * 文件id
     */
    @ApiModelProperty(value = "文件id", required = true)
    private String mainFileId;

    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "$column.columnComment", required = true)
    private String mainFileName;

    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "$column.columnComment", required = true)
    private String fileGroupsVersion;

    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "$column.columnComment", required = true)
    private String fileGroupsVersionName;

    /**
     * 协同人
     */
    @ApiModelProperty(value = "协同人", required = true)
    private String contractCollaborators;

    /**
     * 协同状态
     */
    @ApiModelProperty(value = "协同状态", required = true)
    private String coordinationStatus;

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
     * 文件组json
     */
    private String fileGroupsJson;
    
    /**
     * 文件类型
     */
    private String fileType;
    
}
