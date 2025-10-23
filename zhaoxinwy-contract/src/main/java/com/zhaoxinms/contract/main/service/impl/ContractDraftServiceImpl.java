package com.zhaoxinms.contract.main.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaoxinms.common.core.domain.model.LoginUser;
import com.zhaoxinms.common.utils.DateUtils;
import com.zhaoxinms.common.utils.SecurityUtils;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.contract.conf.exception.ServiceException;
import com.zhaoxinms.contract.conf.util.JsonUtil;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.ContractAuditForm;
import com.zhaoxinms.contract.main.domain.ContractDraft;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftBo;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftFilesBo;
import com.zhaoxinms.contract.main.domain.bo.ContractStatusBo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftFilesVo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftVo;
import com.zhaoxinms.contract.main.enums.ContractEnums;
import com.zhaoxinms.contract.main.mapper.ContractAuditFormMapper;
import com.zhaoxinms.contract.main.mapper.ContractDraftMapper;
import com.zhaoxinms.contract.main.model.ContractDraftFilesModel;
import com.zhaoxinms.contract.main.service.IContractDraftService;
import com.zhaoxinms.contract.main.util.JsonUtils;
import com.zhaoxinms.contract.type.domain.vo.FormVo;
import com.zhaoxinms.contract.type.service.IFormService;
import com.zhaoxinms.contract.type.util.BeanCopyUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;

/**
 * 合同草拟Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-17
 */
@RequiredArgsConstructor
@Service
public class ContractDraftServiceImpl implements IContractDraftService {

	private final ContractDraftMapper baseMapper;
	private final ContractDraftMapper contractDrafterMapper;
	private final ContractDraftFilesServiceImpl contractDraftFilesServiceImpl;
	private final IFormService iFormService;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	@Autowired
	private ContractAuditFormMapper contractAuditFormMapper;
	
	/**
	 * 查询合同草拟
	 */
	@Override
	public ContractDraftVo queryById(String id) {
		ContractDraftVo vo = baseMapper.selectVoById(id);
		ContractDraftFilesVo contractDraftFilesVo = contractDraftFilesServiceImpl.queryDraftFileByContractId(vo.getId(), vo.getFileGroupsVersionName());
		vo.setFileGroup(contractDraftFilesVo == null ? null : contractDraftFilesVo.getFileGroupsJson());
		List<Long> contractType = JsonUtil.getJsonToList(vo.getContractType(), Long.class);
		FormVo form = iFormService.queryById(contractType.get(contractType.size() - 1));
		vo.setForm(form);
		
		//添加最后一次审批的记录
		if (vo.getAuditId()!=null) {
			ContractAuditForm auditForm = contractAuditFormMapper.selectById(vo.getAuditId());
			vo.setAuditForm(auditForm);
		}
		
		return vo;
	}

	/**
	 * 查询我的合同草拟列表
	 */
	@Override
	public TableDataInfo<ContractDraftVo> queryPageList(ContractDraftBo bo, PageQuery pageQuery) {
		LambdaQueryWrapper<ContractDraft> lqw = buildQueryWrapper(bo);
		lqw.eq(ContractDraft::getContractStatus, ContractEnums.ContractStatusEnums.draft.getIndex());
		Page<ContractDraft> result = baseMapper.selectDraftPage(pageQuery.build(), lqw);

	    Page<ContractDraftVo> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
	    if (CollUtil.isEmpty(result.getRecords())) {
	    } else {
	    	voPage.setRecords(BeanCopyUtils.copyList(result.getRecords(), ContractDraftVo.class));
	    }
		return TableDataInfo.build(voPage);
	}

	/**
	 * 查询合同草拟列表
	 */
	@Override
	public List<ContractDraftVo> queryList(ContractDraftBo bo) {
		LambdaQueryWrapper<ContractDraft> lqw = buildQueryWrapper(bo);
		return baseMapper.selectVoList(lqw);
	}
	
	public static String getFulfilStatus(Date effectiveTime, Date endTime,String changeStatus) {
    	if (effectiveTime != null) {
    		if (System.currentTimeMillis() - effectiveTime.getTime() < 0) {
    			return ContractEnums.FulfilStatusEnums.notEffective.getIndex();
    		} else {
    			if (endTime != null) {
    				if (System.currentTimeMillis() - endTime.getTime() < 0) {
    					return ContractEnums.FulfilStatusEnums.effective.getIndex();
    				} else {
    					return ContractEnums.FulfilStatusEnums.expired.getIndex();
    				} 
    			} else {
    				return ContractEnums.FulfilStatusEnums.effective.getIndex();
    			}
    		}
    	} else {
        	return ContractEnums.FulfilStatusEnums.none.getIndex();
    	}
	}

	private LambdaQueryWrapper<ContractDraft> buildQueryWrapper(ContractDraftBo bo) {

		LambdaQueryWrapper<ContractDraft> lqw = Wrappers.lambdaQuery();
		lqw.like(StringUtils.isNotBlank(bo.getContractName()), ContractDraft::getContractName, bo.getContractName());
		lqw.eq(StringUtils.isNotBlank(bo.getContractCode()), ContractDraft::getContractCode, bo.getContractCode());
		lqw.eq(StringUtils.isNotBlank(bo.getContractType()), ContractDraft::getContractType, bo.getContractType());
		lqw.eq(StringUtils.isNotBlank(bo.getContractCollaborators()), ContractDraft::getContractCollaborators,
				bo.getContractCollaborators());
		if(bo.getCreateTimeRange() != null && bo.getCreateTimeRange().size() == 2) {
			lqw.between(ContractDraft::getCreateTime, new Date(bo.getCreateTimeRange().get(0)),new Date( bo.getCreateTimeRange().get(1)));
		}
		lqw.eq(ContractDraft::getDelFlag, "0");
		lqw.orderByDesc(ContractDraft::getUpdateTime);
		return lqw;
	}

	/**
	 * 新增合同草拟
	 * @throws IOException 
	 */
	@Override
	@Transactional
	public Boolean insertByBo(ContractDraftBo bo) throws IOException {

		ContractDraft add = BeanUtil.toBean(bo, ContractDraft.class);
		validEntityBeforeSave(add);
		add.setContractCode(null);
		List<ContractDraftFilesModel> uploadFiles = JsonUtil.getJsonToList(bo.getFileGroup(), ContractDraftFilesModel.class);

		if (StringUtils.isEmpty(bo.getFileGroup())) {
			throw new ServiceException("合同的文件不能为空");
		}
		
		// 验证是否有主文件，没有主文件报错
		boolean hasMainFile = false;
		for (ContractDraftFilesModel model : uploadFiles) {
			if (ContractEnums.FileTypeEnums.mainFile.getIndex().equals(model.getType())) {
				hasMainFile = true;
			}
		}
		if (!hasMainFile) {
			throw new ServiceException("合同文件必须包含一个合同正文");
		}
		
		//查询关联合同信息
		if (StringUtils.isNotEmpty(bo.getRelatedContractId())) {
			ContractDraft related = this.contractDrafterMapper.selectById(bo.getRelatedContractId());
			add.setRelatedContractCode(related.getContractCode());
			add.setRelatedContractId(related.getId());
			add.setRelatedContractName(related.getContractName());
			add.setRelatedTime(new Date());
		}
		
		LoginUser loginUser = SecurityUtils.getLoginUser();
		add.setContractChargeId(loginUser.getUserId().toString());
		add.setContractChargeName(loginUser.getUser().getNickName());
		add.setDrafterName(loginUser.getUser().getNickName());
		add.setDrafterId(loginUser.getUserId().toString());

		boolean flag = baseMapper.insert(add) > 0;
		if (flag) {
			ContractDraftFilesBo contractDraftFilesBo = new ContractDraftFilesBo();
			contractDraftFilesBo.setFileGroupsJson(JsonUtils.toJsonString(uploadFiles));
			contractDraftFilesBo.setContractDraftId(add.getId());
			if (!StringUtil.isNullOrEmpty(bo.getFileGroup())) {
				
				uploadFiles.stream().forEach(fileDetailed -> {
					if (ContractEnums.FileTypeEnums.mainFile.getIndex().equals(fileDetailed.getType())) {
						contractDraftFilesBo.setMainFileId(fileDetailed.getUid());
						contractDraftFilesBo.setMainFileName(fileDetailed.getName());
					}
				});
			}
			contractDraftFilesBo.setFileType(ContractDraftFilesBo.FILE_TYPE_CONTRACT);
			contractDraftFilesServiceImpl.insertByBo(contractDraftFilesBo);
		}
		return flag;
	}
	
	/**
	 * 修改合同草拟
	 */
	@Override
	@Transactional
	public Boolean updateByBo(ContractDraftBo bo) {
		ContractDraft update = BeanUtil.toBean(bo, ContractDraft.class);
		validEntityBeforeSave(update);
		
		List<ContractDraftFilesModel> uploadFiles = JsonUtil.getJsonToList(bo.getFileGroup(), ContractDraftFilesModel.class);

		if (StringUtils.isEmpty(bo.getFileGroup())) {
			throw new ServiceException("合同的文件不能为空");
		}

		// 验证是否有主文件，没有主文件报错
		boolean hasMainFile = false;
		for (ContractDraftFilesModel model : uploadFiles) {
			if (ContractEnums.FileTypeEnums.mainFile.getIndex().equals(model.getType())) {
				hasMainFile = true;
			}
		}
		if (!hasMainFile) {
			throw new ServiceException("合同文件必须包含一个合同正文");
		}
		
		/* 修改合同基本信息时,对合同文件内容进行修改 start */
		ContractDraftFilesVo contractDraftFilesVo = contractDraftFilesServiceImpl.queryDraftFileByContractId(update.getId(),
				update.getFileGroupsVersionName());
		if (contractDraftFilesVo != null) {
			ContractDraftFilesBo contractDraftFilesBo = new ContractDraftFilesBo();
			contractDraftFilesBo.setId(contractDraftFilesVo.getId());
			contractDraftFilesBo.setFileGroupsJson(bo.getFileGroup());
			if (!StringUtil.isNullOrEmpty(bo.getFileGroup())) {
				List<ContractDraftFilesModel> list = JsonUtil.getJsonToList(bo.getFileGroup(),
						ContractDraftFilesModel.class);
				list.stream().forEach(fileDetailed -> {
					if ("0".equals(fileDetailed.getType())) {
						contractDraftFilesBo.setMainFileId(fileDetailed.getUid());
						contractDraftFilesBo.setMainFileName(fileDetailed.getName());
					}
				});
			}
			contractDraftFilesBo.setFileType(ContractDraftFilesBo.FILE_TYPE_CONTRACT);
			
			
			contractDraftFilesServiceImpl.updateByBo(contractDraftFilesBo);
		}
		/* 修改合同基本信息时,对合同文件内容进行修改 end */
		
		

		return baseMapper.updateById(update) > 0;
	}


	/**
	 * 保存前的数据校验
	 */
	private void validEntityBeforeSave(ContractDraft entity) {
	}

	/**
	 * 修改合同状态
	 *
	 * @param bo
	 * @return
	 */
	@Transactional
	public Boolean updateStstusByBo(ContractStatusBo bo) {
		ContractDraft update = BeanUtil.toBean(bo, ContractDraft.class);
		return baseMapper.updateById(update) > 0;
	}

	@Override
	public List<ContractDraft> getClosedContract() throws ParseException {
		String date = DateUtils.parseDateToStr("yyyy-MM-dd", new Date());
		QueryWrapper<ContractDraft> lqw = Wrappers.query();
		lqw.eq("contract_status", ContractEnums.ContractStatusEnums.fulfil.getIndex());//履行中
		lqw.lt("end_time", date);
        List<ContractDraft> list = contractDrafterMapper.selectList(lqw);
		return list;
	}
	
	@Override
	public ContractDraftFilesBo addNewVersion(ContractDraftFilesBo bo) {
		ContractDraft contract = this.baseMapper.selectById(bo.getContractDraftId());
		//验证版本号的格式 v x.x
		if(StringUtils.isEmpty(bo.getFileGroupsVersionName())) {
			throw new ServiceException("新版本号不能为空");
		}
		
		if(!bo.getFileGroupsVersionName().substring(0, 1).equals("V")) {
			throw new ServiceException("版本号必须以大写 V开头");
		}
		float version = 0;
		try {
			version = Float.valueOf(bo.getFileGroupsVersionName().substring(1));
		}catch(Exception e) {
			throw new ServiceException("版本号格式不正确");
		}
		
		float oldVersion = Float.valueOf(contract.getFileGroupsVersionName().substring(1));
		if(oldVersion >= version) {
			throw new ServiceException("新版本号必须大于上一个版本号");
		}
		
		List<ContractDraftFilesModel> uploadFiles = JsonUtil.getJsonToList(bo.getFileGroupsJson(), ContractDraftFilesModel.class);
		if (uploadFiles.size() == 0) {
			throw new ServiceException("合同的文件不能为空");
		}
		uploadFiles.stream().forEach(fileDetailed -> {
			if (ContractEnums.FileTypeEnums.mainFile.getIndex().equals(fileDetailed.getType())) {
				bo.setMainFileId(fileDetailed.getUid());
				bo.setMainFileName(fileDetailed.getName());
			}
		});
		if(StringUtils.isEmpty(bo.getMainFileId())) {
			throw new ServiceException("合同文件必须选择一个主文件");
		}
		
		
		int oldVersionInt = Integer.valueOf(contract.getFileGroupsVersion());
		bo.setFileType(ContractDraftFilesBo.FILE_TYPE_CONTRACT);
		bo.setFileGroupsVersion(oldVersionInt + 1 +"");
		contractDraftFilesServiceImpl.insertByBo(bo);
		
		contract.setFileGroupsVersion(bo.getFileGroupsVersion());
		contract.setFileGroupsVersionName(bo.getFileGroupsVersionName());
		this.baseMapper.updateById(contract);
		return bo;
	}
	
	@Transactional
	@Override
	public void deleteContract(String contractId) {
		ContractDraft draft = this.baseMapper.selectById(contractId);
//		if(!draft.getContractStatus().equals(ContractEnums.ContractStatusEnums.draft.getIndex())) {
//			if(draft.getContractStatus().equals(ContractEnums.ContractStatusEnums.audit.getIndex())
//					&&draft.getContractApproveStatus().equals(ContractEnums.ApproveStatusEnums.reject.getIndex())) {
//				//可以删除
//			} else {
//				throw new ServiceException("该合同不可删除或作废");
//			}
//		}
		LambdaUpdateWrapper<ContractDraft> luw = new LambdaUpdateWrapper<ContractDraft>();
		
		luw.eq(ContractDraft::getId,contractId).
		set(ContractDraft::getDelFlag,"2").
		set(ContractDraft::getContractStatus,ContractEnums.ContractStatusEnums.canceled.getIndex());
		this.baseMapper.update(null, luw);
	}
	

	@Override
	public List<ContractDraft> queryListRepeat(ContractDraftBo bo) {
		QueryWrapper<ContractDraft> lqw = Wrappers.query();
		lqw.eq("contract_name", bo.getContractName());
		lqw.or().eq("contract_code",bo.getContractCode());
		List<ContractDraft> list = contractDrafterMapper.selectList(lqw);
		return list;
	}
}
