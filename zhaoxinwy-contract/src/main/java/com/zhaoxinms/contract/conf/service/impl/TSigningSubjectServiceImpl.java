package com.zhaoxinms.contract.conf.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.contract.conf.domain.TSigningSubject;
import com.zhaoxinms.contract.conf.domain.bo.TSigningSubjectBo;
import com.zhaoxinms.contract.conf.domain.vo.TSigningSubjectVo;
import com.zhaoxinms.contract.conf.exception.ServiceException;
import com.zhaoxinms.contract.conf.mapper.TSigningSubjectMapper;
import com.zhaoxinms.contract.conf.service.ITSigningSubjectService;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

/**
 * 签约主体Service业务层处理
 *
 * @author ruoyi
 * @date 2022-07-12
 */
@RequiredArgsConstructor
@Service
public class TSigningSubjectServiceImpl implements ITSigningSubjectService {

    private final TSigningSubjectMapper baseMapper;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
    /**
     * 签约主体不可用
     */
    private static final String DISABLED = "1";

    /**
     * 查询签约主体
     */
    @Override
    public TSigningSubjectVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询签约主体列表
     */
    @Override
    public TableDataInfo<TSigningSubjectVo> queryPageList(TSigningSubjectBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TSigningSubject> lqw = buildQueryWrapper(bo);
        Page<TSigningSubjectVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询签约主体列表
     */
    @Override
    public List<TSigningSubjectVo> queryList(TSigningSubjectBo bo) {
        LambdaQueryWrapper<TSigningSubject> lqw = buildQueryWrapper(bo);
        List<TSigningSubjectVo> queryList = baseMapper.selectVoList(lqw);
        for (TSigningSubjectVo vo:queryList) {
        	vo.setDisabled(DISABLED.equals(vo.getStatus())?true:false);
		}
        return queryList;
    }

    private LambdaQueryWrapper<TSigningSubject> buildQueryWrapper(TSigningSubjectBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<TSigningSubject> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getSigningSubjectCode()), TSigningSubject::getSigningSubjectCode, bo.getSigningSubjectCode());
        lqw.like(StringUtils.isNotBlank(bo.getSigningSubjectName()), TSigningSubject::getSigningSubjectName, bo.getSigningSubjectName());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessStatus()), TSigningSubject::getBusinessStatus, bo.getBusinessStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), TSigningSubject::getStatus, bo.getStatus());
        lqw.like(StringUtils.isNotBlank(bo.getUscc()), TSigningSubject::getUscc, bo.getUscc());
        lqw.orderByDesc(TSigningSubject::getUpdateTime);
        return lqw;
    }

    /**
     * 新增签约主体
     */
    @Override
    public Boolean insertByBo(TSigningSubjectBo bo) {
        TSigningSubject add = BeanUtil.toBean(bo, TSigningSubject.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改签约主体
     */
    @Override
    public Boolean updateByBo(TSigningSubjectBo bo) {
        TSigningSubject update = BeanUtil.toBean(bo, TSigningSubject.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(TSigningSubject entity){
    	boolean nameExist = false;
    	boolean codeExist = false;
    	if(StringUtils.isBlank(entity.getId())) {
    		nameExist = baseMapper.exists(new LambdaQueryWrapper<TSigningSubject>().
        			eq(TSigningSubject::getSigningSubjectName, entity.getSigningSubjectName()).
        			eq(TSigningSubject::getDelFlag, "0"));
    		codeExist = baseMapper.exists(new LambdaQueryWrapper<TSigningSubject>().
        			eq(TSigningSubject::getSigningSubjectCode, entity.getSigningSubjectCode()).
        			eq(TSigningSubject::getDelFlag, "0"));
    	} else {
    		nameExist = baseMapper.exists(new LambdaQueryWrapper<TSigningSubject>().
        			eq(TSigningSubject::getSigningSubjectName, entity.getSigningSubjectName()).
        			eq(TSigningSubject::getDelFlag, "0").
        			ne(TSigningSubject::getId, entity.getId()));
    		codeExist = baseMapper.exists(new LambdaQueryWrapper<TSigningSubject>().
        			eq(TSigningSubject::getSigningSubjectCode, entity.getSigningSubjectCode()).
        			eq(TSigningSubject::getDelFlag, "0").
        			ne(TSigningSubject::getId, entity.getId()));
    	}
        if (nameExist) {
        	throw new ServiceException("当前签约主体名称已存在");
        } else if(codeExist) {
        	throw new ServiceException("当前签约主体编号已存在");
        }
    }

    /**
     * 批量删除签约主体
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
    	if(ids.size() > 1) {
    		throw new ServiceException("对不起，该模块不支持批量删除");
    	}
        return baseMapper.deleteBatchIds(ids) > 0;
    }

	@Override
	public int updateStatus(TSigningSubjectBo bo) {
		TSigningSubject update = BeanUtil.toBean(bo, TSigningSubject.class);
		return baseMapper.updateById(update) ;
	}

	
}
