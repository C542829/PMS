package com.zhaoxinms.contract.conf.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaoxinms.common.utils.DateUtils;
import com.zhaoxinms.common.utils.SecurityUtils;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.contract.conf.domain.TContractCounterpart;
import com.zhaoxinms.contract.conf.domain.bo.TContractCounterpartBo;
import com.zhaoxinms.contract.conf.domain.vo.TContractCounterpartVo;
import com.zhaoxinms.contract.conf.exception.ServiceException;
import com.zhaoxinms.contract.conf.mapper.TContractCounterpartMapper;
import com.zhaoxinms.contract.conf.service.ITContractCounterpartService;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.conf.util.compare.CompareUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;

/**
 * 合作方Service业务层处理
 *
 * @author ruoyi
 * @date 2022-07-18
 */
@RequiredArgsConstructor
@Service
public class TContractCounterpartServiceImpl implements ITContractCounterpartService {

    private final TContractCounterpartMapper baseMapper;

    /**
     * 查询合作方
     */
    @Override
    public TContractCounterpartVo queryById(String id){
    	TContractCounterpartVo vo = baseMapper.selectVoById(id);
        return vo;
    }

    /**
     * 查询合作方列表
     */
    @Override
    public TableDataInfo<TContractCounterpartVo> queryPageList(TContractCounterpartBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TContractCounterpart> lqw = buildQueryWrapper(bo);
        Page<TContractCounterpartVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }
    
    /**
     * 查询合作方列表
     */
    @Override
    public TableDataInfo<TContractCounterpartVo> queryPermissionPageList(TContractCounterpartBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TContractCounterpart> lqw = buildQueryWrapper(bo);
        Page<TContractCounterpartVo> result = baseMapper.selectPagePartnerByPermission(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }
    
    /**
     * 查询合作方列表
     */
    @Override
    public List<TContractCounterpartVo> queryListWithPermission(TContractCounterpartBo bo) {
        QueryWrapper<TContractCounterpart> qw = Wrappers.query();
        qw.like(StringUtils.isNotEmpty(bo.getContractCounterpartName()), "a.CONTRACT_COUNTERPART_NAME",bo.getContractCounterpartName());
        //qw.like(StringUtils.isNotEmpty(bo.getContractCounterpartCode()), "a.CONTRACT_COUNTERPART_NAME",bo.getContractCounterpartCode());
        qw.like(StringUtils.isNotEmpty(bo.getUscc()), "a.USCC",bo.getUscc());
        qw.eq(StringUtils.isNotEmpty(bo.getBusinessStatus()), "a.BUSINESS_STATUS",bo.getBusinessStatus());
        qw.eq(StringUtils.isNotEmpty(bo.getCompanyType()), "a.COMPANY_TYPE",bo.getCompanyType());
        qw.eq("a.DEL_FLAG","0");
        
        return baseMapper.getListJoinSysUserWithPermission(qw);
    }

    /**
     * 查询合作方列表
     */
    @Override
    public List<TContractCounterpartVo> queryList(TContractCounterpartBo bo) {
        QueryWrapper<TContractCounterpart> qw = Wrappers.query();
        qw.like(StringUtils.isNotEmpty(bo.getContractCounterpartName()), "a.CONTRACT_COUNTERPART_NAME",bo.getContractCounterpartName());
        //qw.like(StringUtils.isNotEmpty(bo.getContractCounterpartCode()), "a.CONTRACT_COUNTERPART_NAME",bo.getContractCounterpartCode());
        qw.like(StringUtils.isNotEmpty(bo.getUscc()), "a.USCC",bo.getUscc());
        qw.eq(StringUtils.isNotEmpty(bo.getBusinessStatus()), "a.BUSINESS_STATUS",bo.getBusinessStatus());
        qw.eq(StringUtils.isNotEmpty(bo.getCompanyType()), "a.COMPANY_TYPE",bo.getCompanyType());
        qw.eq("a.DEL_FLAG","0");
        
        return baseMapper.getListJoinSysUser(qw);
    }

    private LambdaQueryWrapper<TContractCounterpart> buildQueryWrapper(TContractCounterpartBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<TContractCounterpart> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getContractCounterpartName()), TContractCounterpart::getContractCounterpartName, bo.getContractCounterpartName());
        lqw.eq(StringUtils.isNotBlank(bo.getUscc()), TContractCounterpart::getUscc, bo.getUscc());
        //lqw.like(StringUtils.isNotBlank(bo.getContractCounterpartCode()), TContractCounterpart::getContractCounterpartCode, bo.getContractCounterpartCode());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessStatus()), TContractCounterpart::getBusinessStatus, bo.getBusinessStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getCompanyType()), TContractCounterpart::getCompanyType, bo.getCompanyType());
        lqw.eq(TContractCounterpart::getDelFlag, '0');
        lqw.orderByDesc(TContractCounterpart::getCreateTime);
        return lqw;
    }

    /**
     * 新增合作方
     */
    @Override
    public Boolean insertByBo(TContractCounterpartBo bo) {
        TContractCounterpart add = BeanUtil.toBean(bo, TContractCounterpart.class);
        if(add.getForever() == null) {
        	add.setForever(0);
        }
        if(add.getForever() == 1) {
        	add.setBusinessEndDate(null);
        }
        add.setStatus(0);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改合作方
     */
    @Override
    public Boolean updateByBo(TContractCounterpartBo bo) {
        TContractCounterpart update = BeanUtil.toBean(bo, TContractCounterpart.class);
        
        TContractCounterpart contractCounterpart = baseMapper.selectById(bo.getId());
        
        //查询判断是否有权限修改 
        LambdaQueryWrapper<TContractCounterpart> wrapper = new LambdaQueryWrapper<TContractCounterpart>();
        wrapper.eq(TContractCounterpart::getId, bo.getId());
        List<TContractCounterpart> pData= this.baseMapper.selectPartnerByPermission(wrapper);
        if(pData.size() == 0) {
        	throw new ServiceException("您没有权限操作该数据");
        }
        
        
        if(update.getForever() == 1) {
        	LambdaUpdateWrapper<TContractCounterpart> lambdaUpdateWrapper = new LambdaUpdateWrapper<TContractCounterpart>();
        	lambdaUpdateWrapper.eq(TContractCounterpart::getId, bo.getId()).set(TContractCounterpart::getBusinessEndDate, null);
        	baseMapper.update(null, lambdaUpdateWrapper);
        }
        
        validEntityBeforeSave(update);
        String result = new CompareUtils<TContractCounterpart>().compare(contractCounterpart, update);
        JSONObject object = new JSONObject();
        object.put("date", DateUtils.getDate());
        object.put("userId", SecurityUtils.getLoginUser().getUser().getUserId());
        object.put("username", SecurityUtils.getLoginUser().getUser().getNickName());
        object.put("result", result);
        if(StringUtils.isNotEmpty(result)) {
	        if(StringUtils.isNotEmpty(update.getRemarks())) {
	        	JSONArray array = JSONUtil.parseArray(update.getRemarks());
	        	array.add(object);
	        	update.setRemarks(array.toString());
	        } else {
	        	JSONArray array = new JSONArray();
	        	array.add(object);
	        	update.setRemarks(array.toString());
	        }
        }
        
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean updateStauts(TContractCounterpartBo bo) {
        LambdaUpdateWrapper<TContractCounterpart> lambdaUpdateWrapper = new LambdaUpdateWrapper<TContractCounterpart>();
        lambdaUpdateWrapper.eq(TContractCounterpart::getId, bo.getId())
                          .set(TContractCounterpart::getStatus, Integer.parseInt(bo.getStatus()));
        return baseMapper.update(null, lambdaUpdateWrapper) > 0;
    } 

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(TContractCounterpart entity){
    	boolean nameExist = false;
    	boolean codeExist = false;
    	if(StringUtils.isBlank(entity.getId())) {
    		nameExist = baseMapper.exists(new LambdaQueryWrapper<TContractCounterpart>().
        			eq(TContractCounterpart::getContractCounterpartName, entity.getContractCounterpartName()).
        			eq(TContractCounterpart::getDelFlag, "0"));
    	} else {
    		nameExist = baseMapper.exists(new LambdaQueryWrapper<TContractCounterpart>().
        			eq(TContractCounterpart::getContractCounterpartName, entity.getContractCounterpartName()).
        			eq(TContractCounterpart::getDelFlag, "0").
        			ne(TContractCounterpart::getId, entity.getId()));
    	}
        if (nameExist) {
        	throw new ServiceException("当前合作方名称已存在");
        } else if(codeExist) {
        	throw new ServiceException("当前合作方编号已存在");
        }
    }

    /**
     * 批量删除合作方
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
    	if(ids.size() > 1) {
    		throw new ServiceException("对不起，该模块不支持批量删除");
    	}
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
