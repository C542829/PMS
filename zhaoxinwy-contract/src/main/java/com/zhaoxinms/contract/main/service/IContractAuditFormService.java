package com.zhaoxinms.contract.main.service;

import java.util.Collection;
import java.util.List;

import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.main.domain.bo.ContractAuditFormBo;
import com.zhaoxinms.contract.main.domain.vo.ContractAuditFormVo;

/**
 * 合同审批单Service接口
 *
 * @author fanhuibin
 * @date 2022-08-29
 */
public interface IContractAuditFormService {

    /**
     * 查询合同审批单
     */
    ContractAuditFormVo queryById(Long id);

    /**
     * 查询合同审批单列表
     */
    TableDataInfo<ContractAuditFormVo> queryPageList(ContractAuditFormBo bo, PageQuery pageQuery);

    /**
     * 查询合同审批单列表
     */
    List<ContractAuditFormVo> queryList(ContractAuditFormBo bo);

    /**
     * 修改合同审批单
     */
    Boolean updateByBo(ContractAuditFormBo bo);

    /**
     * 校验并批量删除合同审批单信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
