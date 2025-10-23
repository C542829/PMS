package com.zhaoxinms.contract.main.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencentcloudapi.essbasic.v20210526.models.FormField;
import com.zhaoxinms.common.annotation.Log;
import com.zhaoxinms.common.core.controller.BaseController;
import com.zhaoxinms.common.core.domain.entity.SysUser;
import com.zhaoxinms.common.enums.BusinessType;
import com.zhaoxinms.common.utils.DictUtils;
import com.zhaoxinms.common.utils.SecurityUtils;
import com.zhaoxinms.contract.conf.exception.ServiceException;
import com.zhaoxinms.contract.conf.service.ITContractCounterpartService;
import com.zhaoxinms.contract.conf.service.ITSigningSubjectService;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.ContractAuditForm;
import com.zhaoxinms.contract.main.domain.ContractDraft;
import com.zhaoxinms.contract.main.domain.vo.ContractBookVo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftFilesVo;
import com.zhaoxinms.contract.main.enums.ContractEnums;
import com.zhaoxinms.contract.main.mapper.ContractAuditFormMapper;
import com.zhaoxinms.contract.main.mapper.ContractDraftMapper;
import com.zhaoxinms.contract.main.service.IContractAuditService;
import com.zhaoxinms.contract.main.service.IContractDraftFilesService;
import com.zhaoxinms.contract.main.service.impl.ContractDraftServiceImpl;
import com.zhaoxinms.contract.main.util.JsonUtils;
import com.zhaoxinms.contract.type.domain.Form;
import com.zhaoxinms.contract.type.domain.vo.FormVo;
import com.zhaoxinms.contract.type.service.IFormService;
import com.zhaoxinms.contract.type.util.R;
import com.zhaoxinms.system.mapper.SysUserMapper;
import com.zhaoxinms.system.service.ISysUserService;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * 合同Controller
 *
 * @author fanhuibin
 * @date 2022-09-03
 */
@Validated
@Api(value = "合同明细", tags = { "合同明细" })
@RequiredArgsConstructor
@RestController
@RequestMapping("/contract/contractDetail")
public class ContractDetailController extends BaseController {

	private final ContractDraftMapper contractDraftMapper;
	private final ContractAuditFormMapper contractAuditFormMapper;
	private final ITContractCounterpartService iTContractCounterpartService;
	private final ITSigningSubjectService iTSigningSubjectService;
	private final IFormService formService;
	private final IContractDraftFilesService contractDraftFilesService;
	private final SysUserMapper userMapper;
	private final IContractAuditService iContractAuditService;
	private final IFormService iFormService;
	private final ISysUserService userService;

	/**
	 * 查询合同明细
	 * 
	 * @return
	 */
	@ApiOperation("查询合同明细")
	@GetMapping()
	@Log(title = "查看合同明细", businessType = BusinessType.OTHER)
	public R<Map<String, Object>> contractDetail(Long contractId) {
		//Long begin = System.currentTimeMillis();
		ContractDraft draft = contractDraftMapper.selectById(contractId);

		if (!this.PermissionCheck(draft)) {
			throw new ServiceException("您没有权限查阅该文档");
		}

		// 最后的审批单
		ContractAuditForm form = new ContractAuditForm();
		// 获取印章类型
		StringBuffer stamps = new StringBuffer();

		// 获取货币名
		String currencyName = "";
		if (draft.getAuditId() != null) {
			form = contractAuditFormMapper.selectById(draft.getAuditId());

			for (String s : JsonUtils.parseArray(form.getStampType(), String.class)) {
				stamps.append(DictUtils.getDictLabel("SEAL_TYPE", s));
				stamps.append("、");
			}
			currencyName = DictUtils.getDictLabel("CONTRACT_CURRENCY", draft.getCurrency());
		}
		// 查询审批历史数据
		List<ContractAuditForm> auditForms = contractAuditFormMapper.selectList(
				new LambdaQueryWrapper<ContractAuditForm>().eq(ContractAuditForm::getContractId, contractId));

		List<Long> contractTypes = JsonUtils.parseArray(draft.getContractType(), Long.class);
		StringBuffer buffer1 = new StringBuffer();
		for (Long l : contractTypes) {
			buffer1.append(formService.queryById(l).getName());
			buffer1.append("/");
		}

		// 查询合同草稿文件
		ContractDraftFilesVo draftFiles = contractDraftFilesService.queryDraftFileByContractId(draft.getId(),
				draft.getFileGroupsVersionName());
		// 查询合同双章文件
		ContractDraftFilesVo reviewFiles = null;
		if (draft.getSealFilesId() != null) {
			reviewFiles = contractDraftFilesService.queryById("" + draft.getSealFilesId());
		}

		// 查询合同负责人信息
		SysUser user = userMapper.selectUserById(Long.valueOf(draft.getContractChargeId()));
		draft.setContractChargeName(user == null ? "-" : user.getNickName());

		// 解析扩展字段数据
		Form dynamicForm = JsonUtils.parseObject(draft.getExtForm(), Form.class);

		// 获取交接记录
		PageQuery page = new PageQuery();
		page.setPageNum(50);

		// 获取applyUser
		SysUser applyUser = userService.selectUserById(Long.valueOf(form.getApplyUserId()));

		//Long end = System.currentTimeMillis();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("fulfilStatus", ContractDraftServiceImpl.getFulfilStatus(draft.getEffectiveTime(),
				draft.getEndTime(), draft.getContractChangeStatus()));
		result.put("draft", draft);
		result.put("form", form);
		result.put("applyUser", applyUser != null ? applyUser.getNickName() : "");
		result.put("auditForms", auditForms);
		result.put("files", reviewFiles == null ? draftFiles : reviewFiles);
		result.put("contractType", buffer1.substring(0, buffer1.length() - 1));
		result.put("currencyName", currencyName);
		result.put("stamps", stamps.length() != 0 ? stamps.substring(0, stamps.length() - 1) : stamps);
		return R.ok(result);
	}

	// 合同明细的权限控制
	private boolean PermissionCheck(ContractDraft draft) {
		if (draft == null) {
			return false;
		}

		Long loginId = SecurityUtils.getUserId();
		// 1.合同草拟人、负责人随时可查
		if (draft.getDrafterId().equals(loginId.toString()) || draft.getContractChargeId().equals(loginId.toString())) {
			return true;
		}

		// 2.合同审批阶段不做严格权限控制（因为审批人是可变的，暂不做复杂的逻辑控制）
		if (draft.getContractStatus().equals(ContractEnums.ContractStatusEnums.audit.getIndex())) {
			return true;
		}

		// 合同台账部分，根据权限查看是否有权限
		QueryWrapper<ContractDraft> lqw = new QueryWrapper();
		lqw.eq("a.id", draft.getId());
		Page<ContractBookVo> drafts = contractDraftMapper.bookPage(new PageQuery().build(), lqw);
		if (drafts.getRecords().size()>0) {
			return true;
		}

		return false;
	}

	/**
	 * 查询合同审批单明细
	 * 
	 * @return
	 */
	@ApiOperation("查询合同明细")
	@GetMapping("/audit")
	public R<Map<String, Object>> contractAuditDetail(String instanceId) {
		LambdaQueryWrapper<ContractAuditForm> lqw = Wrappers.lambdaQuery();
		lqw.eq(ContractAuditForm::getInstanceId, instanceId);
		ContractAuditForm form = contractAuditFormMapper.selectOne(lqw);

		ContractDraft draft = contractDraftMapper.selectById(form.getContractId());

		// 查询合同草稿文件
		ContractDraftFilesVo draftFiles = contractDraftFilesService.queryDraftFileByContractId(draft.getId(),
				draft.getFileGroupsVersionName());

		// 解析扩展字段数据
		Form dynamicForm = JsonUtils.parseObject(draft.getExtForm(), Form.class);

		// 获取applyUser
		SysUser applyUser = userService.selectUserById(Long.valueOf(form.getApplyUserId()));

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draft", draft);
		result.put("draftFiles", draftFiles);
		result.put("form", form);
		result.put("applyUser", applyUser != null ? applyUser.getNickName() : "");
		return R.ok(result);
	}
	
	@ApiOperation("查询合同文件明细")
	@GetMapping("/contractFiles")
	public R<Map<String, Object>> contractFiles(Long contractId) {
		// TODO 考虑权限问题
		ContractDraft draft = contractDraftMapper.selectById(contractId);
		ContractAuditForm form = new ContractAuditForm();
		if (draft.getAuditId() != null) {
			form = contractAuditFormMapper.selectById(draft.getAuditId());
		}

		// 查询合同草稿文件
		ContractDraftFilesVo draftFiles = contractDraftFilesService.queryDraftFileByContractId(draft.getId(),
				draft.getFileGroupsVersionName());
		// 查询合同双章文件
		ContractDraftFilesVo reviewFiles = null;
		if (draft.getSealFilesId() != null) {
			reviewFiles = contractDraftFilesService.queryById("" + draft.getSealFilesId());
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draft", draft);
		result.put("form", form);
		result.put("files", reviewFiles == null ? draftFiles : reviewFiles);
		return R.ok(result);
	}

}
