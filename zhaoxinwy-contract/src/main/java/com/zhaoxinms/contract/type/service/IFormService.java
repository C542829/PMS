package com.zhaoxinms.contract.type.service;


import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.zhaoxinms.contract.type.domain.bo.FormBo;
import com.zhaoxinms.contract.type.domain.vo.FormVo;

/**
 * 合同类型配置Service接口
 *
 * @author ruoyi
 * @date 2022-07-17
 */
public interface IFormService {

    /**
     * 查询合同类型配置
     */
    FormVo queryById(Long id);


    /**
     * 查询合同类型配置列表
     */
    List<FormVo> queryList(FormBo bo);

    /**
     * 修改合同类型配置
     */
    Boolean insertByBo(FormBo bo);

    /**
     * 修改合同类型配置
     */
    Boolean updateByBo(FormBo bo);

    /**
     * 校验并批量删除合同类型配置信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

	Boolean updateStauts(FormBo bo);
}
