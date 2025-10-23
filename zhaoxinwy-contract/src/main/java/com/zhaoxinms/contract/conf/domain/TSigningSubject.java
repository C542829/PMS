package com.zhaoxinms.contract.conf.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhaoxinms.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 签约主体对象 t_signing_subject
 *
 * @author ruoyi
 * @date 2022-07-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("contract_signing_subject")
public class TSigningSubject extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;
    /**
     * 签约主体编号
     */
    private String signingSubjectCode;
    /**
     * 签约主体名称
     */
    private String signingSubjectName;
    /**
     * 企业经营状态
     */
    private String businessStatus;
    /**
     * 统一社会信用代码
     */
    private String uscc;
    /**
     * 删除标记位
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
    
    private String status;
    
    private String legalPersonName;
    
    private String legalPersonIdCard;

}
