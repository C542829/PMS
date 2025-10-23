package com.zhaoxinms.contract.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaoxinms.contract.main.domain.ContractDraft;
import com.zhaoxinms.contract.main.domain.vo.ContractBookVo;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftVo;
import com.zhaoxinms.contract.type.mapper.BaseMapperPlus;

/**
 * 合同草拟Mapper接口
 *
 * @author ruoyi
 * @date 2022-08-17
 */
public interface ContractDraftMapper extends BaseMapperPlus<ContractDraftMapper, ContractDraft, ContractDraftVo> {
	@Select("SELECT * FROM contract_draft ${ew.customSqlSegment}")
	Page<ContractDraft> selectDraftPage(Page<ContractDraft> page,
			@Param(Constants.WRAPPER) Wrapper<ContractDraft> queryWrapper);
	
	@Select("SELECT a.*,"
			+ "b.create_time as 'auditForm.createTime',"
			+ "b.current_task as 'auditForm.currentTask',"
			+ "b.current_assignee as 'auditForm.currentAssignee', "
			+ "b.instance_id as 'auditForm.instanceId' FROM contract_draft a left join contract_audit_form b on a.audit_id = b.id ${ew.customSqlSegment}")
	Page<ContractDraftVo> selectDraftAuditPage(Page<ContractDraft> page,
			@Param(Constants.WRAPPER) Wrapper<ContractDraft> queryWrapper);

	@Select("SELECT * FROM contract_draft ${ew.customSqlSegment}")
	Page<ContractDraft> selectContractPage(Page<ContractDraft> page,
			@Param(Constants.WRAPPER) Wrapper<ContractDraft> queryWrapper);

	/**
	 * 根据档案获取合同分页列表--必须包含档案
	 * 
	 * @param page
	 * @param wrapper
	 * @return
	 */
	@Select("SELECT a.SUBJECT_NAMES,a.PARTNER_NAMES,a.ID, a.CONTRACT_NAME, a.CONTRACT_CODE, a.CONTRACT_ARCHIVES_ID, b.ARCHIVES_NAME, b.ARCHIVES_CLASSIFIED, a.DRAFTER_NAME, a.CONTRACT_ARCHIVES_TIME "
			+ "from CONTRACT_DRAFT a INNER JOIN CONF_ARCHIVES b on a.CONTRACT_ARCHIVES_ID = b.id "
			+ "${ew.customSqlSegment}")
	public Page<ContractDraftVo> pageContractWidthArchives(Page<ContractDraftVo> page, @Param("ew") Wrapper wrapper);

	/**
	 * 根据档案获取合同分页列表--可以不包含档案
	 * 
	 * @param page
	 * @param wrapper
	 * @return
	 */
	@Select("SELECT a.ID, a.CONTRACT_NAME, a.CONTRACT_CODE, a.CONTRACT_ARCHIVES_ID, b.ARCHIVES_NAME, b.ARCHIVES_CLASSIFIED, a.DRAFTER_NAME, a.CONTRACT_ARCHIVES_TIME,a.CONTRACT_CHARGE_NAME from CONTRACT_DRAFT a LEFT JOIN CONF_ARCHIVES b on a.CONTRACT_ARCHIVES_ID = b.id "
			+ "${ew.customSqlSegment}")
	public Page<ContractDraftVo> pageContractJoinArchives(Page<ContractDraftVo> page, @Param("ew") Wrapper wrapper);

	/**
	 * 合同合并审批数据的列表
	 * 
	 * @param page
	 * @param wrapper
	 * @return
	 */
	@Select("SELECT a.*, "
			+ "b.currency as \"auditForm.currency\", b.inouttype as \"auditForm.inouttype\", b.contract_name as \"auditForm.contractName\", b.amount as \"auditForm.amount\", b.EFFECTIVE_TIME as \"auditForm.effectiveTime\", b.END_TIME as \"auditForm.endTime\", "
			+ "b.partner_names as \"auditForm.partnerNames\""
			+ " from CONTRACT_DRAFT a LEFT JOIN CONTRACT_AUDIT_FORM b on a.audit_id = b.id " + "${ew.customSqlSegment}")
	public Page<ContractDraftVo> pageContractWidthForm(Page<ContractDraftVo> page, @Param("ew") Wrapper wrapper);

	/**
	 * 合同合并审批和任务数据
	 * 
	 * @param page
	 * @param wrapper
	 * @return
	 */
	@Select("SELECT a.*, "
			+ "b.currency as \"auditForm.currency\", b.inouttype as \"auditForm.inouttype\", b.contract_name as \"auditForm.contractName\", b.amount as \"auditForm.amount\", b.EFFECTIVE_TIME as \"auditForm.effectiveTime\", b.END_TIME as \"auditForm.endTime\", "
			+ "b.partner_names as \"auditForm.partnerNames\", done.num as doneTaskNum, undone.num as undoTaskNum"
			+ " from CONTRACT_DRAFT a LEFT JOIN CONTRACT_AUDIT_FORM b on a.audit_id = b.id "
			+ " LEFT JOIN (SELECT CONTRACT_ID,COUNT(1) as num FROM CONTRACT_FULFIL_TASK WHERE TASK_STATUS = '2' AND TYPE='task' GROUP BY CONTRACT_ID ) done on done.CONTRACT_ID = a.ID "
			+ " LEFT JOIN (SELECT CONTRACT_ID,COUNT(1) as num FROM CONTRACT_FULFIL_TASK WHERE TASK_STATUS = '1' AND TYPE='task' GROUP BY CONTRACT_ID ) undone on undone.CONTRACT_ID = a.ID "
			+ "${ew.customSqlSegment}")
	public Page<ContractDraftVo> pageContractWidthTask(Page<ContractDraftVo> page, @Param("ew") Wrapper wrapper);

	/**
	 * 查询合同台账
	 * 
	 * @param page
	 * @param wrapper
	 * @return
	 */
	@Select("SELECT a.*, "
			+ "b.partner_names as \"auditForm.partnerNames\", b.subject_names as \"auditForm.subjectNames\", "
			+ "b.currency as \"auditForm.currency\", b.inouttype as \"auditForm.inouttype\", b.contract_name as \"auditForm.contractName\", b.amount as \"auditForm.amount\", b.EFFECTIVE_TIME as \"auditForm.effectiveTime\", b.END_TIME as \"auditForm.endTime\", "
			+ "b.create_time as \"auditForm.createTime\", "
			+ "a.CONTRACT_APPROVE_PASS_TIME as \"contractApprovePassTime\" "
			+ " "
			+ "from CONTRACT_DRAFT a LEFT JOIN CONTRACT_AUDIT_FORM b on a.audit_id = b.id "
			+ "${ew.customSqlSegment}")
	public Page<ContractBookVo> bookPage(Page<ContractBookVo> page, @Param("ew") Wrapper wrapper);


	@Select("SELECT c.instance_id from CONTRACT_AUDIT_FORM c LEFT JOIN CONTRACT_DRAFT a on a.id = c.contract_id where a.del_flag='2' and instance_id !=null ")
	public List<String> selectDeleteAuditInstanceIds();

	@Select("SELECT c.instance_id from CONTRACT_CHANGE_FORM c LEFT JOIN CONTRACT_DRAFT a on a.id = c.contract_id where a.del_flag='2' and instance_id !=null ")
	public List<String> selectDeleteChangeInstanceIds();
}
