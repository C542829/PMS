package com.zhaoxinms.contract.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.zhaoxinms.contract.main.domain.ContractDraftFiles;
import com.zhaoxinms.contract.main.domain.vo.ContractDraftFilesVo;
import com.zhaoxinms.contract.type.mapper.BaseMapperPlus;

/**
 * 合同文件Mapper接口
 *
 * @author ruoyi
 * @date 2022-08-17
 */
public interface ContractDraftFilesMapper extends BaseMapperPlus<ContractDraftFilesMapper, ContractDraftFiles, ContractDraftFilesVo> {


    /**
     * 查询双章文档
     * @param wrapper
     * @return
     */
    @Select("SELECT f.CONTRACT_DRAFT_ID, f.FILE_GROUPS_JSON "
    		+ "from CONTRACT_DRAFT_FILES f INNER JOIN CONTRACT_DRAFT draft on f.CONTRACT_DRAFT_ID = DRAFT.ID  "
    		+ "" + "${ew.customSqlSegment}")
    public List<ContractDraftFilesVo> listFiles(@Param("ew") Wrapper wrapper);
}
