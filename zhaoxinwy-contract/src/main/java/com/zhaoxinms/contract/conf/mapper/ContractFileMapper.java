package com.zhaoxinms.contract.conf.mapper;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.zhaoxinms.contract.conf.domain.ContractFile;
import com.zhaoxinms.contract.conf.domain.vo.ContractFileVo;
import com.zhaoxinms.contract.type.mapper.BaseMapperPlus;

public interface ContractFileMapper extends BaseMapperPlus<ContractFileMapper, ContractFile, ContractFileVo> {

    // 通过id查询合同，忽略租户
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT * from contract_file where id = #{id}")
    public ContractFile selectByIdInAllTenant(String id);
}
