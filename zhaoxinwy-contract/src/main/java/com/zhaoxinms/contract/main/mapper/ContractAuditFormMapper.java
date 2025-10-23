package com.zhaoxinms.contract.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.zhaoxinms.contract.main.domain.ContractAuditForm;
import com.zhaoxinms.contract.main.domain.vo.ContractAuditFormVo;
import com.zhaoxinms.contract.type.mapper.BaseMapperPlus;

/**
 * 合同审批单Mapper接口
 *
 * @author fanhuibin
 * @date 2022-08-29
 */
public interface ContractAuditFormMapper extends BaseMapperPlus<ContractAuditFormMapper, ContractAuditForm, ContractAuditFormVo> {

    /**
     * 查询审批单文件
     * @param wrapper
     * @return
     */
    @Select("SELECT f.* "
    		+ "from CONTRACT_AUDIT_FORM f INNER JOIN CONTRACT_DRAFT draft on f.CONTRACT_ID = DRAFT.AUDIT_ID  "
    		+ "" + "${ew.customSqlSegment}")
    public List<ContractAuditForm> listAuditFiles(@Param("ew") Wrapper wrapper);
}
