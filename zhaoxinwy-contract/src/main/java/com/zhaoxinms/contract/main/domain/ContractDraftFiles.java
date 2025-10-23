package com.zhaoxinms.contract.main.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhaoxinms.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同文件对象 contract_draft_files
 *
 * @author ruoyi
 * @date 2022-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("contract_draft_files")
public class ContractDraftFiles extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;
    /**
     * 草拟合同id
     */
    private String contractDraftId;
    /**
     * 文件id
     */
    private String mainFileId;
    /**
     * $column.columnComment
     */
    private String mainFileName;
    /**
     * $column.columnComment
     */
    private String fileGroupsVersion;
    /**
     * $column.columnComment
     */
    private String fileGroupsVersionName;
    /**
     * 协同人
     */
    private String contractCollaborators;
    /**
     * 协同状态
     */
    private String coordinationStatus;
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
     * 文件组json
     */
    private String fileGroupsJson;
    /**
     * 合同文件类型
     */
    private String fileType;

}
