package com.zhaoxinms.contract.main.controller;

import java.util.Arrays;

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
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftFilesBo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftFilesVo;
import com.zhaoxinms.contract.main.service.IContractDraftFilesService;
import com.zhaoxinms.contract.type.util.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * 合同文件Controller
 *
 * @author ruoyi
 * @date 2022-08-17
 */
@Validated
@Api(value = "合同文件控制器", tags = {"合同文件管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/contract/contractDraftFiles")
public class ContractDraftFilesController extends BaseController {

    private final IContractDraftFilesService iContractDraftFilesService;

    /**
     * 查询合同文件列表
     */
    @ApiOperation("查询合同文件列表")
    @GetMapping("/list")
    public TableDataInfo<ContractDraftFilesVo> list(ContractDraftFilesBo bo, PageQuery pageQuery) {
        return iContractDraftFilesService.queryPageList(bo, pageQuery);
    }
    
    /**
     * 获取合同文件详细信息
     */
    @ApiOperation("获取合同文件详细信息")
    @GetMapping("/{id}")
    public R<ContractDraftFilesVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") String id) {
        return R.ok(iContractDraftFilesService.queryById(id));
    }

    /**
     * 新增合同文件
     */
    @ApiOperation("新增合同文件")
    @Log(title = "合同文件", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ContractDraftFilesBo bo) {
    	iContractDraftFilesService.insertByBo(bo);
        return R.ok();
    }

    /**
     * 修改合同文件
     */
    @ApiOperation("修改合同文件")
    @Log(title = "合同文件", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ContractDraftFilesBo bo) {
    	iContractDraftFilesService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 删除合同文件
     */
    @ApiOperation("删除合同文件")
    @Log(title = "合同文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                                       @NotEmpty(message = "主键不能为空")
                                       @PathVariable String[] ids) {
    	iContractDraftFilesService.deleteWithValidByIds(Arrays.asList(ids), true);
        return R.ok();
    }
}
