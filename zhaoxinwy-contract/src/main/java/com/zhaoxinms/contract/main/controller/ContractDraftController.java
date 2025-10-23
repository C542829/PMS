package com.zhaoxinms.contract.main.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaoxinms.common.annotation.Log;
import com.zhaoxinms.common.annotation.RepeatSubmit;
import com.zhaoxinms.common.core.controller.BaseController;
import com.zhaoxinms.common.core.validate.AddGroup;
import com.zhaoxinms.common.core.validate.EditGroup;
import com.zhaoxinms.common.enums.BusinessType;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.ContractDraft;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftBo;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftFilesBo;
import com.zhaoxinms.contract.main.domain.bo.ContractStatusBo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftVo;
import com.zhaoxinms.contract.main.enums.ContractEnums;
import com.zhaoxinms.contract.main.mapper.ContractDraftMapper;
import com.zhaoxinms.contract.main.service.IContractDraftService;
import com.zhaoxinms.contract.type.util.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * 合同草拟Controller
 *
 * @author ruoyi
 * @date 2022-08-17
 */
@Validated
@Api(value = "合同草拟控制器", tags = {"合同草拟管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/contract/contractDraft")
public class ContractDraftController extends BaseController {

    private final IContractDraftService iContractDraftService;
    private final ContractDraftMapper contractDraftMapper;
    
	/**
	 * 查询我的待办任务。
	 */
	@ApiOperation("查询我的合同列表")
	@GetMapping("/myContractList")
	public TableDataInfo<ContractDraftVo> myContractList(ContractDraftBo bo, PageQuery pageQuery) {
		QueryWrapper<ContractDraft> lqw = Wrappers.query();
		List<String> status = new ArrayList<String>();
		status.add(ContractEnums.ContractStatusEnums.fulfil.getIndex());
		status.add(ContractEnums.ContractStatusEnums.suspend.getIndex());
		status.add(ContractEnums.ContractStatusEnums.done.getIndex());
		lqw.in("a.contract_status", status);
		lqw.like(StringUtils.isNotBlank(bo.getContractName()), "a.contract_name", bo.getContractName());
		lqw.eq(StringUtils.isNotBlank(bo.getContractCode()), "a.contract_code", bo.getContractCode());
		lqw.like(StringUtils.isNotBlank(bo.getPartnerNames()), "a.partner_names", bo.getPartnerNames());
		
		lqw.eq("a.del_flag", "0");
		lqw.orderByDesc("a.create_time");
		Page<ContractDraftVo> result = contractDraftMapper.pageContractWidthTask(pageQuery.build(), lqw);
		return TableDataInfo.build(result);
	}

    /**
     * 查询已草拟列表
     */
    @ApiOperation("查询草拟列表")
    @GetMapping("/list")
    public TableDataInfo<ContractDraftVo> list(ContractDraftBo bo, PageQuery pageQuery) {
        return iContractDraftService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取合同草拟详细信息
     */
    @ApiOperation("获取合同草拟详细信息")
    @GetMapping("/{id}")
    public R<ContractDraftVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") String id) {
        return R.ok(iContractDraftService.queryById(id));
    }

    /**
     * 新增合同草拟
     * @throws IOException 
     */
    @ApiOperation("新增合同草拟")
    @Log(title = "合同草拟", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ContractDraftBo bo) throws IOException {
    	iContractDraftService.insertByBo(bo);
        return R.ok();
    }
    
    @ApiOperation("上传合同新版本")
    @Log(title = "上传新版本", businessType = BusinessType.INSERT)
    @PostMapping("/newVersion")
    @RepeatSubmit()
    @Transactional
    public R<ContractDraftFilesBo> addNewVersion(@RequestBody ContractDraftFilesBo bo) {
    	return R.ok(iContractDraftService.addNewVersion(bo));
    }

    /**
     * 修改合同草拟
     */
    @ApiOperation("修改合同草拟")
    @Log(title = "合同草拟", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ContractDraftBo bo) {
    	iContractDraftService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 删除合同草拟
     */
    @ApiOperation("删除草拟的合同")
    @Log(title = "合同草拟", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@ApiParam("主键串")
                                       @NotEmpty(message = "主键不能为空")
                                       @PathVariable String id) {
    	iContractDraftService.deleteContract(id);
    	return R.ok();
    }
    
    
    /**
     * 锁定/解锁合同协同
     * @param bo
     * @return
     */
    @ApiOperation("锁定/解锁合同协同")
    @Log(title = "锁定/解锁合同协同", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Void> lockCoordination(@RequestBody ContractStatusBo bo){
    	ContractStatusBo model = new ContractStatusBo();
    	model.setId(bo.getId());
    	model.setCoordinationStatus(bo.getCoordinationStatus());
    	iContractDraftService.updateStstusByBo(bo);
    	return R.ok();
    }
    
}
