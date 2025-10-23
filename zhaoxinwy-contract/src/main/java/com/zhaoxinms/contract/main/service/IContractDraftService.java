package com.zhaoxinms.contract.main.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.ContractDraft;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftBo;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftFilesBo;
import com.zhaoxinms.contract.main.domain.bo.ContractStatusBo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftVo;

/**
 * 合同草拟Service接口
 *
 * @author ruoyi
 * @date 2022-08-17
 */
public interface IContractDraftService {

    /**
     * 查询合同草拟
     */
    ContractDraftVo queryById(String id);

    /**
     * 查询合同草拟列表
     */
    TableDataInfo<ContractDraftVo> queryPageList(ContractDraftBo bo, PageQuery pageQuery);

    /**
     * 查询合同草拟列表
     */
    List<ContractDraftVo> queryList(ContractDraftBo bo);

    /**
     * 修改合同草拟
     * @throws IOException 
     */
    Boolean insertByBo(ContractDraftBo bo) throws IOException;

    /**
     * 修改合同草拟
     */
    Boolean updateByBo(ContractDraftBo bo);

    /**
     * 修改合同状态
     * @param bo
     * @return
     */
    Boolean updateStstusByBo(ContractStatusBo bo);
    
    /**
     * 添加新版本
     */
    ContractDraftFilesBo addNewVersion(ContractDraftFilesBo bo);

    /**
     * 合同删除
     * @param contractId
     */
	void deleteContract(String contractId);

	/**
     * 用于验证合同是否已经存在的查询
     */
    List<ContractDraft> queryListRepeat(ContractDraftBo bo);

	List<ContractDraft> getClosedContract() throws ParseException;
}
