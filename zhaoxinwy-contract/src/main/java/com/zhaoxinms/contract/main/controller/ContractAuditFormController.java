package com.zhaoxinms.contract.main.controller;

import java.util.Arrays;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoxinms.common.annotation.Log;
import com.zhaoxinms.common.annotation.RepeatSubmit;
import com.zhaoxinms.common.core.controller.BaseController;
import com.zhaoxinms.common.core.validate.EditGroup;
import com.zhaoxinms.common.enums.BusinessType;
import com.zhaoxinms.contract.conf.service.ITContractCounterpartService;
import com.zhaoxinms.contract.conf.service.ITSigningSubjectService;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.bo.ContractAuditFormBo;
import com.zhaoxinms.contract.main.domain.vo.ContractAuditFormVo;
import com.zhaoxinms.contract.main.service.IContractAuditFormService;
import com.zhaoxinms.contract.type.util.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * 合同审批单Controller
 *
 * @author fanhuibin
 * @date 2022-08-29
 */
@Validated
@Api(value = "合同审批单控制器", tags = {"合同审批单管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/contract/contractAuditForm")
public class ContractAuditFormController extends BaseController {

    private final IContractAuditFormService iContractAuditFormService;
    
    /**
     * 查询合同审批单列表
     */
    @ApiOperation("查询合同审批单列表")
    @GetMapping("/list")
    public TableDataInfo<ContractAuditFormVo> list(ContractAuditFormBo bo, PageQuery pageQuery) {
        return iContractAuditFormService.queryPageList(bo, pageQuery);
    }
 

    /**
     * 获取合同审批单详细信息
     */
    @ApiOperation("获取合同审批单详细信息")
    @GetMapping("/{id}")
    public R<ContractAuditFormVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iContractAuditFormService.queryById(id));
    }
    
    /**
     * 修改合同审批单
     */
    @ApiOperation("修改合同审批单")
    @Log(title = "合同审批单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ContractAuditFormBo bo) {
    	iContractAuditFormService.updateByBo(bo) ;
        return R.ok();
    }

    /**
     * 删除合同审批单
     */
    @ApiOperation("删除合同审批单")
    @Log(title = "合同审批单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                                       @NotEmpty(message = "主键不能为空")
                                       @PathVariable Long[] ids) {
    	iContractAuditFormService.deleteWithValidByIds(Arrays.asList(ids), true) ;
        return R.ok();
    }
}
