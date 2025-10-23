package com.zhaoxinms.contract.conf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaoxinms.contract.conf.domain.TContractCounterpart;
import com.zhaoxinms.contract.conf.domain.vo.TContractCounterpartVo;
import com.zhaoxinms.contract.type.mapper.BaseMapperPlus;

/**
 * 合作方Mapper接口
 *
 * @author ruoyi
 * @date 2022-07-18
 */
public interface TContractCounterpartMapper extends BaseMapperPlus<TContractCounterpartMapper, TContractCounterpart, TContractCounterpartVo> {


	@Select("select a.*,b.NICK_NAME as headName "
			+"from  CONTRACT_COUNTERPART a left join SYS_USER b on a.CONTRACT_COUNTERPART_HEAD = b.USER_ID "+"${ew.customSqlSegment}")
	List<TContractCounterpartVo> getListJoinSysUser(@Param(Constants.WRAPPER) QueryWrapper<TContractCounterpart> lqw);
	
	@Select("select a.*,b.NICK_NAME as headName "
			+"from  CONTRACT_COUNTERPART a left join SYS_USER b on a.CONTRACT_COUNTERPART_HEAD = b.USER_ID "+"${ew.customSqlSegment}")
	List<TContractCounterpartVo> getListJoinSysUserWithPermission(@Param(Constants.WRAPPER) QueryWrapper<TContractCounterpart> lqw);
	

	@Select("SELECT * FROM contract_counterpart ${ew.customSqlSegment}")
	List<TContractCounterpart> selectPartnerByPermission(@Param(Constants.WRAPPER) Wrapper<TContractCounterpart> queryWrapper);
	
	@Select("SELECT * FROM contract_counterpart ${ew.customSqlSegment}")
	Page<TContractCounterpartVo> selectPagePartnerByPermission(Page<TContractCounterpart> page,@Param(Constants.WRAPPER) Wrapper<TContractCounterpart> queryWrapper);
}
