package com.zhaoxinms.contract.type.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.zhaoxinms.contract.type.util.TreeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同类型配置对象 form
 *
 * @author ruoyi
 * @date 2022-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("contract_type")
public class Form extends TreeEntity<Form> {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 合同类型名
     */
    private String name;
    /**
     * 合同类型编号
     */
    private String code;
    /**
     * 排序字段
     */
    private Long sort;
    /**
     * 表单配置
     */
    private String json;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 动态表单页
     */
    private String vueModule;
    
    /**
     * 流程编号
     */
    private String instanceDefKey;
    
    /**
     * 变更流程编号
     */
    private String changeInstanceDefKey;
    /**
     * 状态 0可用  1不可用
     */
    private Integer status = 1;
}
