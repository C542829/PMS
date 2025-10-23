package com.zhaoxinms.contract.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaoxinms.common.core.controller.BaseController;
import com.zhaoxinms.common.utils.DictUtils;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.ContractDraft;
import com.zhaoxinms.contract.main.domain.bo.ContractBookBo;
import com.zhaoxinms.contract.main.domain.vo.ContractBookVo;
import com.zhaoxinms.contract.main.enums.ContractEnums;
import com.zhaoxinms.contract.main.mapper.ContractDraftMapper;
import com.zhaoxinms.contract.main.service.IContractDraftService;
import com.zhaoxinms.contract.main.util.JsonUtils;
import com.zhaoxinms.contract.type.domain.bo.FormBo;
import com.zhaoxinms.contract.type.domain.vo.FormVo;
import com.zhaoxinms.contract.type.service.IFormService;
import com.zhaoxinms.system.service.ISysConfigService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * 合同台账
 */
@Validated
@Api(value = "合同台账", tags = {"合同台账"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/contract/contractBook")
public class ContractBookController extends BaseController {

    private final ContractDraftMapper contractDraftMapper;
    private final IFormService formService;
    
    
    /**
     * 查询合同台账
     */
    @ApiOperation("查询合同台账")
    @GetMapping("/list")
    public TableDataInfo<ContractBookVo> list(ContractBookBo bo, PageQuery pageQuery) {
    	QueryWrapper<ContractDraft> lqw = this.buildQuery(bo);
    	if("ascending".equals(bo.getCreateTimeOrder())){
    		lqw.orderByAsc("b.create_time");
    	}
    	if("descending".equals(bo.getCreateTimeOrder())){
    		lqw.orderByDesc("b.create_time");
    	}
    	if("ascending".equals(bo.getEffectiveTimeOrder())){
    		lqw.orderByAsc("b.EFFECTIVE_TIME");
    	}
    	if("descending".equals(bo.getEffectiveTimeOrder())){
    		lqw.orderByDesc("b.EFFECTIVE_TIME");
    	}
    	if("ascending".equals(bo.getContractApprovePassTimeOrder())){
    		lqw.orderByAsc("a.CONTRACT_APPROVE_PASS_TIME");
    	}
    	if("descending".equals(bo.getContractApprovePassTimeOrder())){
    		lqw.orderByDesc("a.CONTRACT_APPROVE_PASS_TIME");
    	}
    	if("ascending".equals(bo.getEndTimeOrder())){
    		lqw.orderByAsc("a.end_time");
    	}
    	if("descending".equals(bo.getEndTimeOrder())){
    		lqw.orderByDesc("a.end_time");
    	}
    	
		Page<ContractBookVo> result = contractDraftMapper.bookPage(pageQuery.build(), lqw);
		this.packageData(result.getRecords());
        return TableDataInfo.build(result);
    }
    
    private QueryWrapper<ContractDraft> buildQuery(ContractBookBo bo) {
    	QueryWrapper<ContractDraft> lqw = Wrappers.query();
    	List<String> status = new ArrayList<String>();
		status.add(ContractEnums.ContractStatusEnums.fulfil.getIndex());
		status.add(ContractEnums.ContractStatusEnums.suspend.getIndex());
		status.add(ContractEnums.ContractStatusEnums.done.getIndex());
		status.add(ContractEnums.ContractStatusEnums.signed.getIndex());
		status.add(ContractEnums.ContractStatusEnums.signing.getIndex());
		status.add(ContractEnums.ContractStatusEnums.audit.getIndex());
		status.add(ContractEnums.ContractStatusEnums.canceled.getIndex());
		
		lqw.in("a.contract_status", status);
    	lqw.eq(StringUtils.isNotBlank(bo.getContractName()), "a.contract_name", bo.getContractName());
		lqw.eq(StringUtils.isNotBlank(bo.getContractCode()), "a.contract_code", bo.getContractCode());
		if(bo.getContractTypeArray() != null) {
			List<String> searchArray = new ArrayList<String>();
			for(String s : bo.getContractTypeArray()) {
				List<String> sList = JsonUtils.parseArray("["+s+"]", String.class);
				searchArray.add(JsonUtils.toJsonString(sList));
			}
			lqw.in("a.contract_type", searchArray);
		}
		if(bo.getContractStatus() != null) {
			lqw.in("a.contract_status", bo.getContractStatus());
		}
		if(bo.getEffectiveTimeRange() != null && bo.getEffectiveTimeRange().size() == 2) {
			lqw.between("a.effective_time", new Date(bo.getEffectiveTimeRange().get(0)), new Date(bo.getEffectiveTimeRange().get(1)));
		}
		lqw.eq(StringUtils.isNotBlank(bo.getInouttype()), "a.inouttype", bo.getInouttype());
		lqw.like(StringUtils.isNotBlank(bo.getSubject()), "a.subject_names" , bo.getSubject());
		lqw.like(StringUtils.isNotBlank(bo.getPartner()), "a.partner_names" , bo.getPartner());
		lqw.like(StringUtils.isNotEmpty(bo.getRemarks()), "a.remarks", bo.getRemarks());
		lqw.like(StringUtils.isNotEmpty(bo.getProjectName()), "a.project_name", bo.getProjectName());
		lqw.like(StringUtils.isNotEmpty(bo.getContractChargeName()), "a.contract_charge_name", bo.getContractChargeName());
		lqw.eq("a.del_flag", "0");
		return lqw;
    }
    
    private List<ContractBookVo> packageData(List<ContractBookVo> vos){
    	//查询全部的合同类型
    	List<FormVo> forms = formService.queryList(new FormBo());
    	Map<Long, FormVo> typeMap = forms.stream().collect(Collectors.toMap(FormVo::getId, FormVo -> FormVo));
    	for(ContractBookVo vo:vos) {

			//封装收支类型
			String inoutTypeName = DictUtils.getDictLabel("CONTRACT_IN_OUT_CASH", vo.getInouttype());
			vo.setInouttype(inoutTypeName);
			
			//封装币种信息
			String currencyName = DictUtils.getDictLabel("CONTRACT_CURRENCY", vo.getCurrency());
			vo.setCurrency(currencyName);
			
			//封装合同类型
			List<String> types = JsonUtils.parseArray(vo.getContractType(), String.class);
			vo.setContractTypeName(typeMap.get(Long.valueOf(types.get(types.size()-1))).getName());
			//封装合同状态
			String status = DictUtils.getDictLabel("CONTRACT_STATUS", vo.getContractStatus());
			vo.setContractStatusName(status);
			
			if(StringUtils.isNotEmpty(vo.getInvoiceType())) {
				vo.setInvoiceType(DictUtils.getDictLabel("INVOICE_TYPE", vo.getInvoiceType()));
			}
		}
    	return vos;
    }
   
}
