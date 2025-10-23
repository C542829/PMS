package com.zhaoxinms.contract.type.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhaoxinms.common.exception.ServiceException;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.contract.type.domain.Form;
import com.zhaoxinms.contract.type.domain.bo.FormBo;
import com.zhaoxinms.contract.type.domain.vo.FormVo;
import com.zhaoxinms.contract.type.mapper.FormMapper;
import com.zhaoxinms.contract.type.service.IFormService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;

/**
 * 合同类型配置Service业务层处理
 *
 * @author ruoyi
 * @date 2022-07-17
 */
@RequiredArgsConstructor
@Service
public class FormServiceImpl implements IFormService {

	private final FormMapper baseMapper;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	/**
	 * 根节点id
	 */
	private static final String ROOT_NODE = "0";
	
	

	/**
	 * 查询合同类型配置
	 */
	@Override
	public FormVo queryById(Long id) {
		FormVo vo = baseMapper.selectVoById(id);
		vo.setJsonArray(JSONUtil.parseArray(vo.getJson()));
		return vo;
	}

	/**
	 * 查询合同类型配置列表
	 */
	@Override
	public List<FormVo> queryList(FormBo bo) {
		LambdaQueryWrapper<Form> lqw = buildQueryWrapper(bo);
		List<FormVo> vos = baseMapper.selectVoList(lqw);
		return vos;
	}
	
    @Override
    public Boolean updateStauts(FormBo bo) {
    	Form update = new Form();
        update.setId(bo.getId());
        update.setStatus(Integer.parseInt(bo.getStatus()));
        return baseMapper.updateById(update) > 0;
    }


	private LambdaQueryWrapper<Form> buildQueryWrapper(FormBo bo) {
		Map<String, Object> params = bo.getParams();
		LambdaQueryWrapper<Form> lqw = Wrappers.lambdaQuery();
		lqw.eq(bo.getParentId() != null, Form::getParentId, bo.getParentId());
		lqw.like(StringUtils.isNotBlank(bo.getName()), Form::getName, bo.getName());
		lqw.eq(StringUtils.isNotBlank(bo.getCode()), Form::getCode, bo.getCode());
		lqw.eq(bo.getSort() != null, Form::getSort, bo.getSort());
		lqw.eq(StringUtils.isNotEmpty(bo.getStatus()), Form::getStatus, bo.getStatus() );
		lqw.orderByAsc(Form::getSort);
		return lqw;
	}

	/**
	 * 新增合同类型配置
	 */
	@Override
	public Boolean insertByBo(FormBo bo) {
		Form add = BeanUtil.toBean(bo, Form.class);
		if(bo.getJsonArray() != null) {
			add.setJson(bo.getJsonArray().toString());
		}
		//判断当前合同的上级层数，合同类型不能超过三层
		if(bo.getParentId() != null && !ROOT_NODE.equals(bo.getParentId())){
			Form parentForm = this.baseMapper.selectById(bo.getParentId());
			if(parentForm != null && parentForm.getParentId() != null && !ROOT_NODE.equals(bo.getParentId())) {
				Form superParent = this.baseMapper.selectById(parentForm.getParentId());
				if(parentForm != null && superParent !=null &&superParent.getParentId() != null && !(superParent.getParentId() == 0)) {
					throw new ServiceException("合同分类最多支持三级分类");
				}
			}
		}
		
		validEntityBeforeSave(add);
		boolean flag = baseMapper.insert(add) > 0;
		if (flag) {
			bo.setId(add.getId());
		}
		return flag;
	}

	/**
	 * 修改合同类型配置
	 */
	@Override
	public Boolean updateByBo(FormBo bo) {
		// 只允许修改排序和parentId
		Form update = this.baseMapper.selectById(bo.getId());
		update.setParentId(bo.getParentId());
		update.setSort(bo.getSort());
		update.setJson(bo.getJsonArray().toString());
		//合同类型配置修改，增加可修改合同类型对应的审批流
		update.setInstanceDefKey(bo.getInstanceDefKey());
		update.setChangeInstanceDefKey(bo.getChangeInstanceDefKey());
		validEntityBeforeSave(update);
		return baseMapper.updateById(update) > 0;
	}

	/**
	 * 保存前的数据校验
	 */
	private void validEntityBeforeSave(Form entity) {
		// 查询数据库是否有同名的字段
		FormBo bo = new FormBo();
		bo.setName(entity.getName());
		List<FormVo> sameNameList = this.queryList(bo);
		if (entity.getId() == null && sameNameList.size() > 0) {
			throw new ServiceException("当前名称已被占用，请输入其他名称");
		}

		// 查询数据库是否有同code的字段
		FormBo bo1 = new FormBo();
		bo1.setCode(entity.getCode());
		List<FormVo> sameKeyList = this.queryList(bo1);
		if (entity.getId() == null && sameKeyList.size() > 0) {
			throw new ServiceException("当前编码已被占用，请输入其他编码");
		}
	}

	/**
	 * 批量删除合同类型配置
	 */
	@Override
	public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
		if (isValid) {
			// 有子分类不允许删除父分类
			LambdaQueryWrapper<Form> lqw = Wrappers.lambdaQuery();
			lqw.in(Form::getParentId, ids);
			Long childCount = this.baseMapper.selectCount(lqw);
			if(childCount > 0) {
				throw new ServiceException("当前合同类型包含子类型，不能进行删除");
			}
		}
		if(ids.size() > 1) {
    		throw new ServiceException("对不起，该模块不支持批量删除");
    	}
		
		return baseMapper.deleteBatchIds(ids) > 0;
	}
}
