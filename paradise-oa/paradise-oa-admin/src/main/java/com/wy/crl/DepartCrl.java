package com.wy.crl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Depart;
import com.wy.result.Result;
import com.wy.service.DepartService;
import com.wy.utils.MapUtils;

import io.swagger.annotations.Api;

/**
 * 部门表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "部门表API")
@RestController
@RequestMapping("depart")
public class DepartCrl extends AbstractCrl<Depart, Long> {

	@Autowired
	private DepartService departService;

	/**
	 * 获取部门下拉树列表
	 */
	@GetMapping("treeselect")
	public Result<?> treeselect(Depart dept) {
		List<Depart> depts = departService.getEntitys(dept).getData();
		return Result.ok(departService.buildDeptTreeSelect(depts));
	}

	/**
	 * 加载对应角色部门列表树
	 */
	@GetMapping(value = "/roleDeptTreeselect/{roleId}")
	public Result<?> roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
		List<Depart> depts = departService.getEntitys(new Depart()).getData();
		Map<String, Object> map = MapUtils.getBuilder("checkedKeys", departService.selectDeptListByRoleId(roleId))
				.add("depts", departService.buildDeptTreeSelect(depts)).build();
		return Result.ok(map);
	}
}