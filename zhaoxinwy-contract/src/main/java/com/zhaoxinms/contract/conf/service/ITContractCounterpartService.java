package com.zhaoxinms.contract.conf.service;

import java.util.Collection;
import java.util.List;

import com.zhaoxinms.contract.conf.domain.bo.TContractCounterpartBo;
import com.zhaoxinms.contract.conf.domain.vo.TContractCounterpartVo;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;

/**
 * 合作方Service接口
 *
 * @author ruoyi
 * @date 2022-07-18
 */
public interface ITContractCounterpartService {

    /**
     * 查询合作方
     */
    TContractCounterpartVo queryById(String id);

    /**
     * 查询合作方列表
     */
    TableDataInfo<TContractCounterpartVo> queryPageList(TContractCounterpartBo bo, PageQuery pageQuery);

    /**
     * 查询合作方列表
     */
    List<TContractCounterpartVo> queryList(TContractCounterpartBo bo);

    /**
     * 修改合作方
     */
    Boolean insertByBo(TContractCounterpartBo bo);

    /**
     * 修改合作方
     */
    Boolean updateByBo(TContractCounterpartBo bo);

    /**
     * 校验并批量删除合作方信息
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

    Boolean updateStauts(TContractCounterpartBo bo);

	/**
	 * 查询合作方列表
	 */
	TableDataInfo<TContractCounterpartVo> queryPermissionPageList(TContractCounterpartBo bo, PageQuery pageQuery);

	/**
	 * 查询合作方列表
	 */
	List<TContractCounterpartVo> queryListWithPermission(TContractCounterpartBo bo);
}
