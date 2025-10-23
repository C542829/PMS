package com.zhaoxinms.contract.main.service;

import java.util.List;

import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.ContractAuditForm;
import com.zhaoxinms.contract.main.domain.bo.ContractDraftBo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftVo;

/**
 * 合同审批单Service接口
 *
 * @author fanhuibin
 * @date 2022-08-29
 */
public interface IContractAuditService {

    /**
     * 合同开始审批
     */
    boolean beginAudit(ContractAuditForm form);

    /**
     * 审批通过
     * @return
     */
    void complete(Long contractId);
    
    /**
     * 审批驳回
     * @return
     */
    void reject(Long contractId);
    
    /**
     * 审批撤回
     * @return
     */
    void cancel(Long contractId);

	/**
	 * 查询我的审批中的合同
	 */
	TableDataInfo<ContractDraftVo> queryPageList(ContractDraftBo bo, PageQuery pageQuery);

	/**
	 * 退回到草稿箱
	 * @param valueOf
	 */
	void backToDraft(Long valueOf);
	

	/**
	 * 关闭合同
	 * @param contractId
	 */
	void closeContract(Long contractId);

	/**
	 * 合同作废
	 * @param draft
	 */
	void cancelContract(ContractDraftBo draft);

}
