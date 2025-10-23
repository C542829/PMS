package com.zhaoxinms.contract.main.service;

import java.util.Collection;
import java.util.List;

import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftFilesBo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftFilesVo;
import com.zhaoxinms.contract.main.model.ContractDraftFilesModel;

/**
 * 合同文件Service接口
 *
 * @author ruoyi
 * @date 2022-08-17
 */
public interface IContractDraftFilesService {

    /**
     * 查询合同文件
     */
    ContractDraftFilesVo queryById(String id);

    /**
     * 查询合同文件列表
     */
    TableDataInfo<ContractDraftFilesVo> queryPageList(ContractDraftFilesBo bo, PageQuery pageQuery);

    /**
     * 查询合同文件列表
     */
    List<ContractDraftFilesVo> queryList(ContractDraftFilesBo bo);

    /**
     * 修改合同文件
     */
    Boolean insertByBo(ContractDraftFilesBo bo);

    /**
     * 修改合同文件
     */
    Boolean updateByBo(ContractDraftFilesBo bo);

    /**
     * 校验并批量删除合同文件信息
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);


    ContractDraftFilesVo queryDraftFileByContractId(String contractDraftId,String fileGroupsVersionName);

	/**
	 * 将一组文件合并到另一组中
	 * @param toFilesId
	 * @param fromFileId
	 */
	void mergeFiles(String toFilesId, String fromFileId);

	/**
	 * 将一组文件合并到另一组中
	 * @param toFilesId
	 * @param fromFileId
	 */
	void mergeFiles(String toFilesId, List<ContractDraftFilesModel> fromFiles);

}
