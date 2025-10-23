package com.zhaoxinms.contract.conf.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoxinms.contract.conf.util.TreeSelectModel;
import com.zhaoxinms.contract.type.domain.bo.FormBo;
import com.zhaoxinms.contract.type.domain.vo.FormVo;
import com.zhaoxinms.contract.type.service.IFormService;
import com.zhaoxinms.contract.type.util.R;

@RestController
@RequestMapping("/conf/treeSelect")
public class TreeSelectorController {

	@Autowired
	private IFormService formService;

	@RequestMapping("/contractTypeSelect")
	public R<List<TreeSelectModel>> contractTypeSelect(FormBo bo) {
		List<TreeSelectModel> result = new ArrayList<TreeSelectModel>();

		List<FormVo> list = formService.queryList(bo);
		for (FormVo vo : list) {
			TreeSelectModel tree = new TreeSelectModel();
			tree.setType("user");
			tree.setParentId("" + vo.getParentId());
			tree.setId("" + vo.getId());
			tree.setName(vo.getName());
			result.add(tree);
		}

		Map<String, List<TreeSelectModel>> ParentIdMap = result.stream()
				.collect(Collectors.groupingBy(TreeSelectModel::getParentId));
		result.forEach(tree -> {
			tree.setChildren(ParentIdMap.get(tree.getId()));
		});
		result = result.stream().filter(v -> v.getParentId().equals("0")).collect(Collectors.toList());

		return R.ok(result);
	}
}
