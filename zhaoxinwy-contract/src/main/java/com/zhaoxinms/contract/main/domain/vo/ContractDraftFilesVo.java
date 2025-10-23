package com.zhaoxinms.contract.main.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 合同文件视图对象 contract_draft_files
 *
 * @author ruoyi
 * @date 2022-08-17
 */
@Data
@ApiModel("合同文件视图对象")
@ExcelIgnoreUnannotated
public class ContractDraftFilesVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private String id;

    /**
     * 草拟合同id
     */
    @ExcelProperty(value = "草拟合同id")
    @ApiModelProperty("草拟合同id")
    private String contractDraftId;

    /**
     * 文件id
     */
    @ExcelProperty(value = "文件id")
    @ApiModelProperty("文件id")
    private String mainFileId;

    private String mainFileName;

    private String fileGroupsVersion;

    private String fileGroupsVersionName;

    /**
     * 协同人
     */
    @ExcelProperty(value = "协同人")
    @ApiModelProperty("协同人")
    private String contractCollaborators;

    /**
     * 协同状态
     */
    @ExcelProperty(value = "协同状态")
    @ApiModelProperty("协同状态")
    private String coordinationStatus;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ApiModelProperty("备注")
    private String remarks;

    /**
     * 租户id
     */
    @ExcelProperty(value = "租户id")
    @ApiModelProperty("租户id")
    private String tenantId;

    /**
     * 文件组json
     */
    private String fileGroupsJson;

    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 创建者
     */
    private String createBy;
}
