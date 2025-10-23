package com.zhaoxinms.contract.main.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaoxinms.contract.conf.util.JsonUtil;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.ContractDraftFiles;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftFilesBo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftFilesVo;
import com.zhaoxinms.contract.main.mapper.ContractDraftFilesMapper;
import com.zhaoxinms.contract.main.model.ContractDraftFilesModel;
import com.zhaoxinms.contract.main.service.IContractDraftFilesService;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

/**
 * 合同文件Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-17
 */
@RequiredArgsConstructor
@Service
public class ContractDraftFilesServiceImpl implements IContractDraftFilesService {

    private final ContractDraftFilesMapper baseMapper;

    /**
     * 查询合同文件
     */
    @Override
    public ContractDraftFilesVo queryById(String id){
        return baseMapper.selectVoById(id);
    }
    
    /**
     * 将一组文件合并到另一组中
     * @param toFilesId
     * @param fromFileId
     */
    @Transactional
    @Override
    public void mergeFiles(String toFilesId, String fromFileId) {
    	ContractDraftFiles from = this.baseMapper.selectById(fromFileId);
    	List<ContractDraftFilesModel> fromFiles = JsonUtil.getJsonToList(from.getFileGroupsJson(), ContractDraftFilesModel.class);
    	this.mergeFiles(toFilesId, fromFiles);
    }
    
    /**
     * 将一组文件合并到另一组中
     * @param toFilesId
     * @param fromFileId
     */
    @Transactional
    @Override
    public void mergeFiles(String toFilesId, List<ContractDraftFilesModel> fromFiles) {
    	ContractDraftFiles to = this.baseMapper.selectById(toFilesId);
    	List<ContractDraftFilesModel> toFiles = JsonUtil.getJsonToList(to.getFileGroupsJson(), ContractDraftFilesModel.class);
    	for (ContractDraftFilesModel model:fromFiles) {
    		//目前只有变更才会合并文件
    		model.setFrom("change");
    		model.setType(ContractDraftFilesModel.ATTACHMENT_FILE);
    		toFiles.add(model);
    	}
    	to.setFileGroupsJson(JsonUtil.getObjectToString(toFiles));
    	this.baseMapper.updateById(to);
    }
    
    /**
     * 根据草拟合同id和合同版本查询合同文件
     */
    @Override
    public ContractDraftFilesVo queryDraftFileByContractId(String contractDraftId,String fileGroupsVersionName){
    	LambdaQueryWrapper<ContractDraftFiles> lqw = Wrappers.lambdaQuery();
    	lqw.eq(ContractDraftFiles::getContractDraftId, contractDraftId);
    	lqw.eq(ContractDraftFiles::getFileGroupsVersionName, fileGroupsVersionName);
    	lqw.eq(ContractDraftFiles::getFileType, ContractDraftFilesBo.FILE_TYPE_CONTRACT);
    	return baseMapper.selectVoOne(lqw);
    }
    
    /**
     * 查询合同文件列表
     */
    @Override
    public TableDataInfo<ContractDraftFilesVo> queryPageList(ContractDraftFilesBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ContractDraftFiles> lqw = buildQueryWrapper(bo);
        Page<ContractDraftFilesVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询合同文件列表
     */
    @Override
    public List<ContractDraftFilesVo> queryList(ContractDraftFilesBo bo) {
        LambdaQueryWrapper<ContractDraftFiles> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ContractDraftFiles> buildQueryWrapper(ContractDraftFilesBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ContractDraftFiles> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getContractDraftId()), ContractDraftFiles::getContractDraftId, bo.getContractDraftId());
        lqw.eq(StringUtils.isNotBlank(bo.getMainFileId()), ContractDraftFiles::getMainFileId, bo.getMainFileId());
        lqw.like(StringUtils.isNotBlank(bo.getMainFileName()), ContractDraftFiles::getMainFileName, bo.getMainFileName());
        lqw.eq(StringUtils.isNotBlank(bo.getFileGroupsVersion()), ContractDraftFiles::getFileGroupsVersion, bo.getFileGroupsVersion());
        lqw.like(StringUtils.isNotBlank(bo.getFileGroupsVersionName()), ContractDraftFiles::getFileGroupsVersionName, bo.getFileGroupsVersionName());
        lqw.eq(StringUtils.isNotBlank(bo.getContractCollaborators()), ContractDraftFiles::getContractCollaborators, bo.getContractCollaborators());
        lqw.eq(StringUtils.isNotBlank(bo.getCoordinationStatus()), ContractDraftFiles::getCoordinationStatus, bo.getCoordinationStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRemarks()), ContractDraftFiles::getRemarks, bo.getRemarks());
        lqw.eq(StringUtils.isNotBlank(bo.getTenantId()), ContractDraftFiles::getTenantId, bo.getTenantId());
        lqw.orderByDesc(ContractDraftFiles::getUpdateTime);
        return lqw;
    }

    /**
     * 新增合同文件
     */
    @Override
    public Boolean insertByBo(ContractDraftFilesBo bo) {
        ContractDraftFiles add = BeanUtil.toBean(bo, ContractDraftFiles.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改合同文件
     */
    @Override
    public Boolean updateByBo(ContractDraftFilesBo bo) {
        ContractDraftFiles update = BeanUtil.toBean(bo, ContractDraftFiles.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }
    

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ContractDraftFiles entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除合同文件
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
    
    
}
