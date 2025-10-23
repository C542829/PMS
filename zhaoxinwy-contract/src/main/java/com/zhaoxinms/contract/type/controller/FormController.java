package com.zhaoxinms.contract.type.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoxinms.common.annotation.Log;
import com.zhaoxinms.common.annotation.RepeatSubmit;
import com.zhaoxinms.common.core.controller.BaseController;
import com.zhaoxinms.common.core.validate.AddGroup;
import com.zhaoxinms.common.core.validate.EditGroup;
import com.zhaoxinms.common.enums.BusinessType;
import com.zhaoxinms.contract.type.domain.bo.FormBo;
import com.zhaoxinms.contract.type.domain.vo.FormVo;
import com.zhaoxinms.contract.type.service.IFormService;
import com.zhaoxinms.contract.type.util.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * 合同类型配置Controller
 *
 * @author ruoyi
 * @date 2022-07-17
 */
@Validated
@Api(value = "合同类型配置控制器", tags = {"合同类型配置管理"})
@RequiredArgsConstructor
@RestController 
@RequestMapping("/system/form")
public class FormController extends BaseController {

    private final IFormService iFormService;

    /**
     * 查询合同类型配置列表
     */
    @ApiOperation("查询合同类型配置列表")
    @GetMapping("/list")
    public R<List<FormVo>> list(FormBo bo) {
        List<FormVo> list = iFormService.queryList(bo);
        return R.ok(list);
    }

    /**
     * 获取合同类型配置详细信息
     */
    @ApiOperation("获取合同类型配置详细信息")
    @GetMapping("/{id}")
    public R<FormVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iFormService.queryById(id));
    }

    /**
     * 新增合同类型配置
     */
    @ApiOperation("新增合同类型配置")
    @Log(title = "合同类型配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody FormBo bo) {
    	iFormService.insertByBo(bo);
        return R.ok();
    }

    /**
     * 修改合同类型配置
     */
    @ApiOperation("修改合同类型配置")
    @Log(title = "合同类型配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody FormBo bo) {
    	iFormService.updateByBo(bo);
        return R.ok();
    }
    
    /**
     * 状态修改
     */
    @ApiOperation("状态修改")
    @Log(title = "合同类型状态修改", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody FormBo bo) {
    	iFormService.updateStauts(bo);
        return R.ok();
    }

    /**
     * 删除合同类型配置
     */
    @ApiOperation("删除合同类型配置")
    @Log(title = "合同类型配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                                       @NotEmpty(message = "主键不能为空")
                                       @PathVariable Long[] ids) {
    	iFormService.deleteWithValidByIds(Arrays.asList(ids), true) ;
        return R.ok();
    }
    
}
