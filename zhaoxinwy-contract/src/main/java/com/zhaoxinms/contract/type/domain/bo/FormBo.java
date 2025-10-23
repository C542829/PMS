package com.zhaoxinms.contract.type.domain.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.zhaoxinms.common.core.validate.AddGroup;
import com.zhaoxinms.common.core.validate.EditGroup;
import com.zhaoxinms.contract.type.util.TreeEntity;

import cn.hutool.json.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同类型配置业务对象 form
 *
 * @author ruoyi
 * @date 2022-07-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("合同类型配置业务对象")
public class FormBo extends TreeEntity<FormBo> {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 合同类型名
     */
    @ApiModelProperty(value = "合同类型名", required = true)
    @NotBlank(message = "合同类型名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 合同类型编号
     */
    @ApiModelProperty(value = "合同类型编号", required = true)
    @NotBlank(message = "合同类型编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段", required = true)
    @NotNull(message = "排序字段不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;
    
    /**
     * 表单字段
     */
    @ApiModelProperty(value = "表单字段")
    private JSONArray jsonArray;
    
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
     * 可用状态 0可用 1不可用
     */
    private String status;
}
