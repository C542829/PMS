package com.zhaoxinms.contract.main.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.ContractAuditForm;
import com.zhaoxinms.contract.main.domain.bo.ContractAuditFormBo;
import com.zhaoxinms.contract.main.domain.vo.ContractAuditFormVo;
import com.zhaoxinms.contract.main.mapper.ContractAuditFormMapper;
import com.zhaoxinms.contract.main.service.IContractAuditFormService;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

/**
 * 合同审批单Service业务层处理
 *
 * @author fanhuibin
 * @date 2022-08-29
 */
@RequiredArgsConstructor
@Service
public class ContractAuditFormServiceImpl implements IContractAuditFormService {

	private final ContractAuditFormMapper baseMapper;

	/**
	 * 查询合同审批单
	 */
	@Override
	public ContractAuditFormVo queryById(Long id) {
		return baseMapper.selectVoById(id);
	}

	/**
	 * 查询合同审批单列表
	 */
	@Override
	public TableDataInfo<ContractAuditFormVo> queryPageList(ContractAuditFormBo bo, PageQuery pageQuery) {
		LambdaQueryWrapper<ContractAuditForm> lqw = buildQueryWrapper(bo);
		Page<ContractAuditFormVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
		return TableDataInfo.build(result);
	}

	/**
	 * 查询合同审批单列表
	 */
	@Override
	public List<ContractAuditFormVo> queryList(ContractAuditFormBo bo) {
		LambdaQueryWrapper<ContractAuditForm> lqw = buildQueryWrapper(bo);
		return baseMapper.selectVoList(lqw);
	}

	private LambdaQueryWrapper<ContractAuditForm> buildQueryWrapper(ContractAuditFormBo bo) {
		Map<String, Object> params = bo.getParams();
		LambdaQueryWrapper<ContractAuditForm> lqw = Wrappers.lambdaQuery();
		lqw.eq(bo.getContractId() != null, ContractAuditForm::getContractId, bo.getContractId());
		lqw.eq(StringUtils.isNotBlank(bo.getSubjects()), ContractAuditForm::getSubjects, bo.getSubjects());
		lqw.eq(StringUtils.isNotBlank(bo.getPartners()), ContractAuditForm::getPartners, bo.getPartners());
		lqw.eq(StringUtils.isNotBlank(bo.getCurrency()), ContractAuditForm::getCurrency, bo.getCurrency());
		lqw.eq(bo.getAmount() != null, ContractAuditForm::getAmount, bo.getAmount());
		lqw.eq(StringUtils.isNotBlank(bo.getInouttype()), ContractAuditForm::getInouttype, bo.getInouttype());
		lqw.eq(bo.getProjectId() != null, ContractAuditForm::getProjectId, bo.getProjectId());
		lqw.eq(bo.getEffectiveTime() != null, ContractAuditForm::getEffectiveTime, bo.getEffectiveTime());
		lqw.eq(bo.getEndTime() != null, ContractAuditForm::getEndTime, bo.getEndTime());
		lqw.eq(StringUtils.isNotBlank(bo.getStampOrder()), ContractAuditForm::getStampOrder, bo.getStampOrder());
		lqw.eq(StringUtils.isNotBlank(bo.getStampType()), ContractAuditForm::getStampType, bo.getStampType());
		lqw.eq(StringUtils.isNotBlank(bo.getApplyUserId()), ContractAuditForm::getApplyUserId, bo.getApplyUserId());
		lqw.eq(StringUtils.isNotBlank(bo.getApplyDept()), ContractAuditForm::getApplyDept, bo.getApplyDept());
		lqw.orderByDesc(ContractAuditForm::getCreateTime);
		return lqw;
	}

	/**
	 * 修改合同审批单
	 */
	@Override
	public Boolean updateByBo(ContractAuditFormBo bo) {
		ContractAuditForm update = BeanUtil.toBean(bo, ContractAuditForm.class);
		return baseMapper.updateById(update) > 0;
	}
	

	/**
	 * 批量删除合同审批单
	 */
	@Override
	public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
		if (isValid) {
			// TODO 做一些业务上的校验,判断是否需要校验
		}
		return baseMapper.deleteBatchIds(ids) > 0;
	}
}
