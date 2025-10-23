package com.zhaoxinms.contract.conf.service;

import java.util.Collection;
import java.util.List;

import com.zhaoxinms.contract.conf.domain.bo.TSigningSubjectBo;
import com.zhaoxinms.contract.conf.domain.vo.TSigningSubjectVo;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;

/**
 * 签约主体Service接口
 *
 * @author ruoyi
 * @date 2022-07-12
 */
public interface ITSigningSubjectService {

    /**
     * 查询签约主体
     */
    TSigningSubjectVo queryById(String id);

    /**
     * 查询签约主体列表
     */
    TableDataInfo<TSigningSubjectVo> queryPageList(TSigningSubjectBo bo, PageQuery pageQuery);

    /**
     * 查询签约主体列表
     */
    List<TSigningSubjectVo> queryList(TSigningSubjectBo bo);

    /**
     * 修改签约主体
     */
    Boolean insertByBo(TSigningSubjectBo bo);

    /**
     * 修改签约主体
     */
    Boolean updateByBo(TSigningSubjectBo bo);

    /**
     * 校验并批量删除签约主体信息
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

    /**
     * 修改印章状态
     * @param bo
     * @return
     */
	int updateStatus(TSigningSubjectBo bo);
}
