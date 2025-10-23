package com.zhaoxinms.contract.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoxinms.common.annotation.Log;
import com.zhaoxinms.common.annotation.RepeatSubmit;
import com.zhaoxinms.common.core.controller.BaseController;
import com.zhaoxinms.common.core.validate.AddGroup;
import com.zhaoxinms.common.enums.BusinessType;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.contract.conf.domain.bo.TContractCounterpartBo;
import com.zhaoxinms.contract.conf.domain.bo.TSigningSubjectBo;
import com.zhaoxinms.contract.conf.domain.vo.TContractCounterpartVo;
import com.zhaoxinms.contract.conf.domain.vo.TSigningSubjectVo;
import com.zhaoxinms.contract.conf.exception.ServiceException;
import com.zhaoxinms.contract.conf.service.ITContractCounterpartService;
import com.zhaoxinms.contract.conf.service.ITSigningSubjectService;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.ContractAuditForm;
import com.zhaoxinms.contract.main.domain.ContractDraft;
import com.zhaoxinms.contract.main.domain.bo.ContractAuditFormBo;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftBo;
import com.zhaoxinms.contract.main.domain.vo.ContractAuditFormVo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftVo;
import com.zhaoxinms.contract.main.mapper.ContractDraftMapper;
import com.zhaoxinms.contract.main.service.IContractAuditFormService;
import com.zhaoxinms.contract.main.service.IContractAuditService;
import com.zhaoxinms.contract.type.util.R;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
@Api(value = "合同审批申请", tags = { "合同审批申请管理" })
@RequiredArgsConstructor
@RestController
@RequestMapping("/contract/contractAudit")
public class ContractAuditController extends BaseController {

	private final IContractAuditFormService iContractAuditFormService;
	private final IContractAuditService iContractAuditService;
	private final ITSigningSubjectService iTSigningSubjectService;
	private final ITContractCounterpartService iTContractCounterpartService;
	private final ContractDraftMapper contractDraftMapper;

	/**
	 * 查询合同审批列表
	 */
	@ApiOperation("查询合同审批列表")
	@GetMapping("/list")
	public TableDataInfo<ContractDraftVo> list(ContractDraftBo bo, PageQuery pageQuery) {

		return iContractAuditService.queryPageList(bo, pageQuery);
	}

	@ApiOperation("查询签约主体")
	@PostMapping("/searchSubject")
	public R<List<TSigningSubjectVo>> searchConfig(@RequestBody JSONObject json) {
		String text = json.getStr("text");
		TSigningSubjectBo bo = new TSigningSubjectBo();
		bo.setSigningSubjectName(text);
		bo.setStatus("0"); // 可用状态
		List<TSigningSubjectVo> list = iTSigningSubjectService.queryList(bo);
		return R.ok(list);
	}

	@ApiOperation("查询合作方")
	@PostMapping("/searchPartner")
	public R<List<TContractCounterpartVo>> searchPartner(@RequestBody JSONObject json) {
		String text = json.getStr("text");
		TContractCounterpartBo bo = new TContractCounterpartBo();
		bo.setContractCounterpartName(text);
		List<TContractCounterpartVo> list = iTContractCounterpartService.queryList(bo);
		return R.ok(list);
	}

	@ApiOperation("通过id查询合作方和签约主体")
	@PostMapping("/searchMerchantById")
	public R<Map<String, Object>> searchMerchantById(@RequestBody JSONObject json) {
		String partner = json.getStr("partner");
		String subject = json.getStr("subject");

		List<TSigningSubjectVo> subjectList = new ArrayList<TSigningSubjectVo>();
		List<TContractCounterpartVo> partnerList = new ArrayList<TContractCounterpartVo>();

		JSONArray partnerIds = JSONUtil.parseArray(partner);
		JSONArray subjectIds = JSONUtil.parseArray(subject);

		for (int i = 0; i < partnerIds.size(); i++) {
			String partnerId = partnerIds.getStr(i);
			partnerList.add(this.iTContractCounterpartService.queryById(partnerId));
		}

		for (int i = 0; i < subjectIds.size(); i++) {
			String subjectId = subjectIds.getStr(i);
			subjectList.add(this.iTSigningSubjectService.queryById(subjectId));
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("partner", partnerList);
		map.put("subject", subjectList);
		
		return R.ok(map);
	}

	/**
	 * 获取合同审批单详细信息
	 */
	@ApiOperation("获取合同审批单详细信息")
	@GetMapping("/{id}")
	public R<ContractAuditFormVo> getInfo(@ApiParam("主键") @NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
		return R.ok(iContractAuditFormService.queryById(id));
	}

	/**
	 * 审批成功
	 */
	@ApiOperation("审批成功")
	@Log(title = "审批成功", businessType = BusinessType.INSERT)
	@RepeatSubmit()
	@PostMapping("/auditSuccess")
	public R<Void> auditSuccess(@RequestBody ContractDraft draft) {
		iContractAuditService.complete(Long.valueOf(draft.getId()));
		return R.ok();
	}

	/**
	 * 审批失败
	 */
	@ApiOperation("审批失败")
	@Log(title = "审批失败", businessType = BusinessType.INSERT)
	@RepeatSubmit()
	@PostMapping("/auditFail")
	public R<Void> auditFail(@RequestBody ContractDraft draft) {
		iContractAuditService.reject(Long.valueOf(draft.getId()));
		return R.ok();
	}

	/**
	 * 重新编辑
	 */
	@ApiOperation("重新编辑")
	@Log(title = "重新编辑", businessType = BusinessType.INSERT)
	@RepeatSubmit()
	@PostMapping("/backToDraft")
	public R<Void> backToDraft(@RequestBody ContractDraft draft) {
		iContractAuditService.backToDraft(Long.valueOf(draft.getId()));
		return R.ok();
	}

	/**
	 * 发起审批
	 */
	@ApiOperation("提交合同审批单")
	@Log(title = "提交合同审批单", businessType = BusinessType.INSERT)
	@RepeatSubmit()
	@PostMapping()
	public R<Void> add(@Validated(AddGroup.class) @RequestBody ContractAuditFormBo bo) {
		ContractAuditForm form = BeanUtil.toBean(bo, ContractAuditForm.class);
		iContractAuditService.beginAudit(form);
		return R.ok();
	}

	
	/**
	 * 合同作废
	 */
	@ApiOperation("合同作废")
	@Log(title = "合同作废", businessType = BusinessType.INSERT)
	@RepeatSubmit()
	@PostMapping("/cancelContract")
	public R<Void> cancelContract(@RequestBody ContractDraftBo draft) {
		iContractAuditService.cancelContract(draft);
		return R.ok();
	}

	/**
	 * 合同续期
	 */
	@ApiOperation("合同续期预览")
	@GetMapping("/renewInfo/{contractId}")
	public R<Map<String, String>> renewInfo(@PathVariable String contractId) {
		ContractDraft draft = this.contractDraftMapper.selectById(contractId);
		if (draft.getEndTime() == null) {
			throw new ServiceException("该合同没有设置截止时间，请手动输入截止时间");
		}
		Date newEndTime = DateUtil.offset(draft.getEndTime(), DateField.YEAR, 1);
		Map<String, String> map = new HashMap<String, String>();
		map.put("newEndTime", DateUtil.format(newEndTime, "yyyy-MM-dd"));
		return R.ok(map);
	}

	/**
	 * 合同续期
	 */
	@ApiOperation("合同续期")
	@PostMapping("/renew/{contractId}")
	@Log(title = "合同续期", businessType = BusinessType.INSERT)
	@RepeatSubmit()
	@Transactional
	public R<Map<String, String>> renew(@PathVariable String contractId, @RequestBody String json) {
		JSONObject object = JSONUtil.parseObj(json);
		if (!object.containsKey("newEndTime")) {
			throw new ServiceException("合同截止时间不能为空");
		}
		if (StringUtils.isEmpty(object.getStr("newEndTime"))) {
			throw new ServiceException("合同截止时间不能为空");
		}
		ContractDraft draft = this.contractDraftMapper.selectById(contractId);
		Date newEndTime = DateUtil.parse(object.getStr("newEndTime"), "yyyy-MM-dd");
		if (newEndTime.getTime() - draft.getEffectiveTime().getTime() < 0) {
			throw new ServiceException("合同截止时间不能小于合同开始时间");
		}
		draft.setEndTime(newEndTime);
		this.contractDraftMapper.updateById(draft);
		return R.ok();
	}
}
