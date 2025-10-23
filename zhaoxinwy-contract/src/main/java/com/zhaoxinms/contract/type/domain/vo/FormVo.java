package com.zhaoxinms.contract.type.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;



/**
 * 合同类型配置视图对象 form
 *
 * @author ruoyi
 * @date 2022-07-17
 */
@Data
@ApiModel("合同类型配置视图对象")
@ExcelIgnoreUnannotated
public class FormVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 父id
     */
    @ExcelProperty(value = "父id")
    @ApiModelProperty("父id")
    private Long parentId;

    /**
     * 合同类型名
     */
    @ExcelProperty(value = "合同类型名")
    @ApiModelProperty("合同类型名")
    private String name;

    /**
     * 合同类型编号
     */
    @ExcelProperty(value = "合同类型编号")
    @ApiModelProperty("合同类型编号")
    private String code;

    /**
     * 排序字段
     */
    @ExcelProperty(value = "排序字段")
    @ApiModelProperty("排序字段")
    private Long sort;

    /**
     * 表单配置
     */
    @ExcelProperty(value = "表单配置")
    @ApiModelProperty("表单配置")
    private String json;

    /**
     * 表单字段
     */
    private JSONArray jsonArray;
    
    /**
     * 动态表单数据解析结果
     */
    private JSONObject formArray;
    
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
     * 流程名称
     */
    private String instanceDefKeyName;
    /**
     * 变更流程名称
     */
    private String changeInstanceDefKeyName;
    
    /**
     * 可用状态 0可用 1不可用
     */
    private Integer status;
}
