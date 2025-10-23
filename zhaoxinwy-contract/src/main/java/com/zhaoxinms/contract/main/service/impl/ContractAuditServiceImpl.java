package com.zhaoxinms.contract.main.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaoxinms.base.service.BillRuleService;
import com.zhaoxinms.common.exception.ServiceException;
import com.zhaoxinms.common.utils.SecurityUtils;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.contract.conf.service.ITContractCounterpartService;
import com.zhaoxinms.contract.conf.service.ITSigningSubjectService;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.ContractAuditForm;
import com.zhaoxinms.contract.main.domain.ContractDraft;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftBo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftVo;
import com.zhaoxinms.contract.main.enums.ContractEnums;
import com.zhaoxinms.contract.main.mapper.ContractAuditFormMapper;
import com.zhaoxinms.contract.main.mapper.ContractDraftMapper;
import com.zhaoxinms.contract.main.service.IContractAuditService;
import com.zhaoxinms.contract.main.service.IContractDraftFilesService;
import com.zhaoxinms.contract.main.util.JsonUtils;
import com.zhaoxinms.contract.type.service.IFormService;
import com.zhaoxinms.system.service.ISysConfigService;
import com.zhaoxinms.system.service.ISysDictDataService;
import com.zhaoxinms.system.service.ISysUserService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContractAuditServiceImpl implements IContractAuditService {

	private final ContractAuditFormMapper baseMapper;
	private final ContractDraftMapper draftMapper;
	private final ContractDraftMapper contractDraftMapper;
	private final ITContractCounterpartService iTContractCounterpartService;
	private final ITSigningSubjectService iTSigningSubjectService;
	public static final String BILL_CODE = "hetongdanhao";
	@Autowired
	private final ISysUserService iSysUserService;
	@Autowired
    private ISysDictDataService iSysDictDataService;
	/**
	 * 用户可用状态为“不可用”
	 */
	private static final String USER_STATUS = "1";
	private final IFormService formService;
	private final ISysConfigService sysConfigService;
	private static final String FLOW_CODE = "htlc";
    @Autowired
    private BillRuleService billRuleService;

	
	/**
	 * 查询我的审批中的合同
	 */
	@Override
	public TableDataInfo<ContractDraftVo> queryPageList(ContractDraftBo bo, PageQuery pageQuery) {
		QueryWrapper<ContractDraft> lqw = new QueryWrapper<ContractDraft>();
		
		lqw.like(StringUtils.isNotBlank(bo.getContractName()), "a.contract_name", bo.getContractName());
		lqw.like(StringUtils.isNotBlank(bo.getPartnerNames()), "a.partner_names", bo.getPartnerNames());
		lqw.like(StringUtils.isNotBlank(bo.getContractCode()), "a.contract_code", bo.getContractCode());
		lqw.eq(StringUtils.isNotBlank(bo.getContractType()), "a.contract_type", bo.getContractType());
		lqw.like(StringUtils.isNotEmpty(bo.getDrafterName()), "a.drafter_name", bo.getDrafterName());
		if (bo.getCreateTimeRange() != null && bo.getCreateTimeRange().size() == 2) {
			lqw.between("b.create_time", new Date(bo.getCreateTimeRange().get(0)),
					new Date(bo.getCreateTimeRange().get(1)));
		}

		lqw.eq("a.drafter_id",  SecurityUtils.getUserId());
		lqw.orderByDesc("b.create_time");

		List<String> status = new ArrayList<String>();
		status.add(ContractEnums.ContractStatusEnums.audit.getIndex());
		status.add(ContractEnums.ContractStatusEnums.signed.getIndex());
		status.add(ContractEnums.ContractStatusEnums.signing.getIndex());
		status.add(ContractEnums.ContractStatusEnums.done.getIndex());
		status.add(ContractEnums.ContractStatusEnums.fulfil.getIndex());
		status.add(ContractEnums.ContractStatusEnums.suspend.getIndex());
		lqw.in("a.contract_status", status);
		lqw.eq("a.del_flag", "0");
		Page<ContractDraftVo> result = draftMapper.selectDraftAuditPage(pageQuery.build(), lqw);
		
		return TableDataInfo.build(result);
	}

	@Override
	@Transactional
	public boolean beginAudit(ContractAuditForm form) {
		ContractDraft contract = contractDraftMapper.selectById("" + form.getContractId());
		ContractAuditForm add = BeanUtil.toBean(form, ContractAuditForm.class);
		
		// 合同状态为拟定中
		if (!contract.getContractStatus().equals(ContractEnums.ContractStatusEnums.draft.getIndex())) {
			throw new ServiceException("拟定中的合同才能发起审批");
		}
		// 审批状态是未审批状态
		if (!contract.getContractApproveStatus().equals(ContractEnums.ApproveStatusEnums.none.getIndex())) {
			throw new ServiceException("当前合同不能发起审批");
		}
		
		// 审批的流程单号不能为空
		if (StringUtils.isEmpty(add.getInstanceDefKey())) {
			add.setInstanceDefKey(FLOW_CODE);
		}
		
		// 合同结束时间不为空的话，结束时间不能小于开始时间
		if (form.getEndTime() != null) {
			if (form.getEndTime().getTime() - form.getEffectiveTime().getTime() < 0) {
				throw new ServiceException("合同截止时间不能小于合同的生效时间");
			}
		}
		
		//查询关联合同信息
		if (StringUtils.isNotEmpty(form.getRelatedContractId())) {
			ContractDraft related = this.draftMapper.selectById(form.getRelatedContractId());
			add.setRelatedContractCode(related.getContractCode());
			add.setRelatedContractId(related.getId());
			add.setRelatedContractName(related.getContractName());
			add.setRelatedTime(new Date());
			
			contract.setRelatedContractCode(related.getContractCode());
			contract.setRelatedContractId(related.getId());
			contract.setRelatedContractName(related.getContractName());
			contract.setRelatedTime(new Date());
		}
		
		//校验税率数据
		if (StringUtils.isNotEmpty(form.getTax())) {
				add.setTax(form.getTax());
				add.setTaxAmount(form.getTaxAmount());
				add.setExcludeTaxAmount(form.getExcludeTaxAmount());
				add.setInvoiceType(form.getInvoiceType());
				contract.setTax(form.getTax());
				contract.setTaxAmount(form.getTaxAmount());
				contract.setExcludeTaxAmount(form.getExcludeTaxAmount());
				contract.setInvoiceType(form.getInvoiceType());
		}
		
		//检验资质信息，如果有资质信息提醒用户必须要选择公章
		boolean hasGZ = false;
		if (StringUtils.isNotEmpty(add.getCertificateType())) {
			JSONArray certificateArray = JSONUtil.parseArray(add.getCertificateType());
			JSONArray array = JSONUtil.parseArray(add.getStampType());
			if(certificateArray.size()>0) {
				for (int i=0;i<array.size();i++) {
					if(array.getStr(i).equals("0")) {
						hasGZ = true;
					}
				}
				if(hasGZ) {
					//跳过
				} else {
					throw new ServiceException("资质文件需要盖公章，请在印章类型中增加公章");
				}
			}
		}

		// 设置发起人和发起部门信息，不能以用户提交的为主
		add.setApplyUserId("" + SecurityUtils.getLoginUser().getUser().getUserId());
		add.setApplyUserName(SecurityUtils.getLoginUser().getUser().getNickName());
		add.setApplyDept("" + SecurityUtils.getLoginUser().getDeptId());
		add.setApplyDeptName(SecurityUtils.getLoginUser().getUser().getDept().getDeptName());

		// 补充签约主体、合作方的名称
		List<String> subjectIds = JsonUtils.parseArray(add.getSubjects(), String.class);
		StringBuffer names = new StringBuffer();
		for (String l : subjectIds) {
			names.append(iTSigningSubjectService.queryById(l).getSigningSubjectName());
			names.append("、");
		}
		add.setSubjectNames(names.substring(0, names.length() - 1));

		// 补充合作方信息
		List<String> partnerIds = JsonUtils.parseArray(add.getPartners(), String.class);
		StringBuffer partnerNames = new StringBuffer();
		for (String l : partnerIds) {
			partnerNames.append(iTContractCounterpartService.queryById(l).getContractCounterpartName());
			partnerNames.append("、");
		}
		add.setPartnerNames(partnerNames.substring(0, partnerNames.length() - 1));
		
	    //如果inouttype==0，金额设置为0
		if(add.getInouttype().equals("0")) {
			add.setAmount("0");
		}
		Map<String,String> map = new HashMap<String,String>();
		add.setAuditStatus(ContractEnums.ApproveStatusEnums.progress.getIndex());
		add.setCertificateType(form.getCertificateType());
		
		boolean flag = baseMapper.insert(add) > 0;

		// 发起审批之后同步修改合同的状态、审批状态为审批中
		contract.setContractApproveStatus(ContractEnums.ApproveStatusEnums.progress.getIndex());
		contract.setContractStatus(ContractEnums.ContractStatusEnums.audit.getIndex());
		contract.setContractApproveApplyTime(new Date());
		
		//同步审批数据到合同主文件
		contract.setAmount(add.getAmount());
		contract.setEndTime(add.getEndTime());
		contract.setEffectiveTime(add.getEffectiveTime());
		contract.setSubjects(add.getSubjects());
		contract.setPartners(add.getPartners());
		contract.setInouttype(add.getInouttype());
		contract.setCurrency(add.getCurrency());
		contract.setExtData(add.getExtData());
		contract.setExtForm(add.getExtForm());
		contract.setAuditId(add.getId());
		contract.setSubjectNames(add.getSubjectNames());
		contract.setPartnerNames(add.getPartnerNames());
		
		// 判断合同是否生成了编号，如果没有就生成编号
		if (StringUtils.isEmpty(contract.getContractCode())) {
			String code = billRuleService.getBillNumber(BILL_CODE, false);
			contract.setContractCode(code);
		}
		
		if (add.getProjectId() != null) {
			contract.setProjectCode(add.getProjectCode());
			contract.setProjectId(add.getProjectId().toString());
			contract.setProjectManager(add.getProjectManager());
			contract.setProjectName(add.getProjectName());
		} else {
			//如果合同已经有项目信息也需要清空
			contract.setProjectCode("");
			contract.setProjectId("");
			contract.setProjectManager("");
			contract.setProjectName("");
		}
		contract.setIsTemplate(add.getIsTemplate());
		contract.setMoneyPeriod(add.getMoneyPeriod());
		contract.setCertificateType(add.getCertificateType());
		contract.setRenewal(add.getRenewal());
		contract.setRenewalText(add.getRenewalText());
		contract.setRemarks(add.getRemarks());
		this.setContractToFulfil(contract);
		contractDraftMapper.updateById(contract);
		
		if (flag) {
			form.setId(add.getId());
		}
		return flag;
	}
	
	/**
	 * 目前该系统没有增加审批功能，直接设置为审批通过
	 */
	private void setContractToFulfil(ContractDraft contract) {
		// 发起审批之后同步修改合同的状态、审批状态为审批中
		contract.setContractApproveStatus(ContractEnums.ApproveStatusEnums.pass.getIndex());
		contract.setContractStatus(ContractEnums.ContractStatusEnums.fulfil.getIndex());
	}

	@Override
	@Transactional
	public void complete(Long contractId) {
		ContractDraft contract = contractDraftMapper.selectById("" + contractId);
		if (!contract.getContractStatus().equals(ContractEnums.ContractStatusEnums.audit.getIndex())) {
			throw new ServiceException("当前合同不处于审批中的状态");
		}
		// 审批状态是审批中
		if (!contract.getContractApproveStatus().equals(ContractEnums.ApproveStatusEnums.progress.getIndex())) {
			throw new ServiceException("当前合同状态不正确");
		}

		contract.setContractApproveStatus(ContractEnums.ApproveStatusEnums.pass.getIndex());
		contract.setContractApprovePassTime(new Date());
		contractDraftMapper.updateById(contract);
		
		//更新审批单状态为审批通过
		ContractAuditForm form = this.baseMapper.selectById(contract.getAuditId());
		form.setAuditStatus(ContractEnums.ApproveStatusEnums.pass.getIndex());
		
		//清空审批流程数据
		form.setCurrentTask("");
		form.setCurrentAssignee("");
		
		this.baseMapper.updateById(form);
		
	}

	@Override
	@Transactional
	public void reject(Long contractId) {
		ContractDraft contract = contractDraftMapper.selectById("" + contractId);
		if (!contract.getContractStatus().equals(ContractEnums.ContractStatusEnums.audit.getIndex())) {
			throw new ServiceException("当前合同不处于审批中的状态");
		}
		// 审批状态是审批中
		if (!contract.getContractApproveStatus().equals(ContractEnums.ApproveStatusEnums.progress.getIndex())) {
			throw new ServiceException("当前合同状态不正确");
		}
		contract.setContractApproveStatus(ContractEnums.ApproveStatusEnums.reject.getIndex());
		contractDraftMapper.updateById(contract);
		
		//设置审批单状态为不通过
		ContractAuditForm form = this.baseMapper.selectById(contract.getAuditId());
		form.setAuditStatus(ContractEnums.ApproveStatusEnums.reject.getIndex());
		form.setCurrentTask("");
		form.setCurrentAssignee("");
		
		this.baseMapper.updateById(form);
		
	}

	@Override
	@Transactional
	public void cancel(Long contractId) {
		ContractDraft contract = contractDraftMapper.selectById("" + contractId);
		if (!contract.getContractStatus().equals(ContractEnums.ContractStatusEnums.audit.getIndex())) {
			throw new ServiceException("当前合同不处于审批中的状态");
		}
		// 审批状态是审批中
		if (!contract.getContractApproveStatus().equals(ContractEnums.ApproveStatusEnums.progress.getIndex())) {
			throw new ServiceException("当前合同状态不正确");
		}
		contract.setContractApproveStatus(ContractEnums.ApproveStatusEnums.cancel.getIndex());
		contractDraftMapper.updateById(contract);
		
		//设置审批单状态为不通过
		ContractAuditForm form = this.baseMapper.selectById(contract.getAuditId());
		form.setAuditStatus(ContractEnums.ApproveStatusEnums.cancel.getIndex());
		form.setCurrentTask("");
		form.setCurrentAssignee("");
		
		this.baseMapper.updateById(form);
		
		this.backToDraft(contractId);
	}


	private LambdaQueryWrapper<ContractDraft> buildQueryWrapper(ContractDraftBo bo) {
		Map<String, Object> params = bo.getParams();
		LambdaQueryWrapper<ContractDraft> lqw = Wrappers.lambdaQuery();
		lqw.like(StringUtils.isNotBlank(bo.getContractName()), ContractDraft::getContractName, bo.getContractName());
		lqw.like(StringUtils.isNotBlank(bo.getContractCode()), ContractDraft::getContractCode, bo.getContractCode());
		lqw.eq(StringUtils.isNotBlank(bo.getContractType()), ContractDraft::getContractType, bo.getContractType());
		lqw.eq(StringUtils.isNotBlank(bo.getContractCollaborators()), ContractDraft::getContractCollaborators,
				bo.getContractCollaborators());
		if (bo.getCreateTimeRange() != null && bo.getCreateTimeRange().size() == 2) {
			lqw.between(ContractDraft::getCreateTime, new Date(bo.getCreateTimeRange().get(0)),
					new Date(bo.getCreateTimeRange().get(1)));
		}

		lqw.orderByDesc(ContractDraft::getCreateTime);
		return lqw;
	}

	@Override
	@Transactional
	public void backToDraft(Long contractId) {
		ContractDraft contract = contractDraftMapper.selectById("" + contractId);
		if (!contract.getContractStatus().equals(ContractEnums.ContractStatusEnums.audit.getIndex())
				&&!contract.getContractStatus().equals(ContractEnums.ContractStatusEnums.signing.getIndex())) {
			throw new ServiceException("当前合同不处于审批中/待签署的状态");
		}

		contract.setContractStatus(ContractEnums.ContractStatusEnums.draft.getIndex());
		contract.setContractApproveStatus(ContractEnums.ApproveStatusEnums.none.getIndex());
		this.contractDraftMapper.updateById(contract);
	}

	/**
	 * 关闭合同
	 * 
	 * @param contractId
	 */
	@Override
	@Transactional
	public void closeContract(Long contractId) {
		ContractDraft contract = contractDraftMapper.selectById("" + contractId);
		// 履约状态的合同才可以关闭
		if (!contract.getContractStatus().equals(ContractEnums.ContractStatusEnums.fulfil.getIndex())) {
			throw new ServiceException("当前合同状态不正确不能关闭");
		}

		// 合同的截止时间为空或者合同已经截止才能关闭
		if (contract.getEndTime() != null && contract.getEndTime().getTime() > System.currentTimeMillis()) {
			throw new ServiceException("合同还未到截止时间，不能关闭");
		}
		
		//合同如果在变更，提示变更中不能关闭
		if(contract.getContractChangeStatus().equals(ContractEnums.ContractStatusEnums.audit.getIndex())) {
			throw new ServiceException("该合同正在变更中关闭失败");
		}
		
		contract.setContractStatus(ContractEnums.ContractStatusEnums.done.getIndex());
		this.contractDraftMapper.updateById(contract);
	}


	/**
	 * 合同作废(用印之后作废)
	 */
	@Transactional
	public void cancelContract(ContractDraftBo draft) {
		
		ContractDraft contract = contractDraftMapper.selectById("" + draft.getId());
		
		if(!contract.getContractStatus().equals(ContractEnums.ContractStatusEnums.draft.getIndex())) {
			throw new ServiceException("该合同不可作废");
		}
		
		LambdaUpdateWrapper<ContractDraft> luw = new LambdaUpdateWrapper<ContractDraft>();

		luw.eq(ContractDraft::getId,draft.getId()).
		set(ContractDraft::getContractStatus,ContractEnums.ContractStatusEnums.canceled.getIndex()).
		set(ContractDraft::getContractCancelRemark,draft.getContractCancelRemark());
		
		contractDraftMapper.update(null, luw);
		
	}
}
