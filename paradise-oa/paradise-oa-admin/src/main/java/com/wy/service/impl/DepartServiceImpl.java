package com.wy.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.base.Tree;
import com.wy.common.UserConstants;
import com.wy.mapper.DepartMapper;
import com.wy.model.Depart;
import com.wy.result.ResultException;
import com.wy.service.DepartService;
import com.wy.utils.StrUtils;

/**
 * 部门表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Service("departService")
public class DepartServiceImpl extends AbstractService<Depart, Long> implements DepartService {

	@Autowired
	private DepartMapper departMapper;

	/**
	 * @apiNote 查询本级菜单信息或直接下级菜单信息 FIXME
	 * @param id 菜单编号
	 * @param parent 是否为上级菜单编号
	 * @return 本级或直接下级菜单列表
	 */
	// @Override
	// public List<Map<String, Object>> getTree(long id, boolean parent) {
	// StringBuffer sql = new StringBuffer("select a.depart_id treeId,a.depart_name
	// treeName,")
	// .append("(select count(*) from ti_depart where pid = a.depart_id) childNum ")
	// .append(" from ti_depart a where ").append(parent ? " a.pid=@id " :
	// "a.depart_id = @id")
	// .append(" order by a.depart_id");
	// return parent ? departMapper.getTreeById(id) : departMapper.getTreeById(id);
	// }

	/**
	 * 构建前端所需要下拉树结构
	 * 
	 * @param depts 部门列表
	 * @return 下拉树结构列表
	 */
	@Override
	public List<Tree<Depart, Long>> buildDeptTreeSelect(List<Depart> depts) {
		List<Depart> deptTrees = buildDeptTree(depts);
		return deptTrees.stream().map(Tree::new).collect(Collectors.toList());
	}

	/**
	 * 构建前端所需要树结构
	 * 
	 * @param depts 部门列表
	 * @return 树结构列表
	 */
	@Override
	public List<Depart> buildDeptTree(List<Depart> depts) {
		List<Depart> returnList = new ArrayList<Depart>();
		List<Long> tempList = new ArrayList<Long>();
		for (Depart dept : depts) {
			tempList.add(dept.getDepartId());
		}
		for (Iterator<Depart> iterator = depts.iterator(); iterator.hasNext();) {
			Depart dept = (Depart) iterator.next();
			// 如果是顶级节点, 遍历该父节点的所有子节点
			if (!tempList.contains(dept.getPid())) {
				recursionFn(depts, dept);
				returnList.add(dept);
			}
		}
		if (returnList.isEmpty()) {
			returnList = depts;
		}
		return returnList;
	}

	/**
	 * 根据角色ID查询部门树信息
	 * 
	 * @param roleId 角色ID
	 * @return 选中部门列表
	 */
	@Override
	public List<Integer> selectDeptListByRoleId(Long roleId) {
		return departMapper.selectDeptListByRoleId(roleId);
	}

	/**
	 * 是否存在子节点
	 * 
	 * @param deptId 部门ID
	 * @return 结果
	 */
	@Override
	public boolean hasChildByDeptId(Long deptId) {
		int result = departMapper.hasChildByDeptId(deptId);
		return result > 0 ? true : false;
	}

	/**
	 * 查询部门是否存在用户
	 * 
	 * @param deptId 部门ID
	 * @return 结果 true 存在 false 不存在
	 */
	@Override
	public boolean checkDeptExistUser(Long deptId) {
		int result = departMapper.checkDeptExistUser(deptId);
		return result > 0 ? true : false;
	}

	/**
	 * 校验部门名称是否唯一
	 * 
	 * @param dept 部门信息
	 * @return 结果
	 */
	@Override
	public String checkDeptNameUnique(Depart dept) {
		Long deptId = Objects.isNull(dept.getDepartId()) ? -1L : dept.getDepartId();
		Depart info = departMapper.checkDeptNameUnique(dept.getDepartName(), dept.getPid());
		if (Objects.nonNull(info) && info.getDepartId().longValue() != deptId.longValue()) {
			return UserConstants.NOT_UNIQUE;
		}
		return UserConstants.UNIQUE;
	}

	/**
	 * 修改子元素关系
	 * 
	 * @param deptId 被修改的部门ID
	 * @param newAncestors 新的父ID集合
	 * @param oldAncestors 旧的父ID集合
	 */
	public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
		List<Depart> children = departMapper.selectChildrenDeptById(deptId);
		if (children.size() > 0) {
			departMapper.updateDeptChildren(children);
		}
	}

	/**
	 * 递归列表
	 */
	private void recursionFn(List<Depart> list, Depart t) {
		// 得到子节点列表
		List<Depart> childList = getChildList(list, t);
		t.setChildren(childList);
		for (Depart tChild : childList) {
			if (hasChild(list, tChild)) {
				// 判断是否有子节点
				Iterator<Depart> it = childList.iterator();
				while (it.hasNext()) {
					Depart n = (Depart) it.next();
					recursionFn(list, n);
				}
			}
		}
	}

	/**
	 * 得到子节点列表
	 */
	private List<Depart> getChildList(List<Depart> list, Depart t) {
		List<Depart> tlist = new ArrayList<Depart>();
		Iterator<Depart> it = list.iterator();
		while (it.hasNext()) {
			Depart n = it.next();
			if (Objects.nonNull(n.getPid()) && n.getPid().longValue() == t.getDepartId().longValue()) {
				tlist.add(n);
			}
		}
		return tlist;
	}

	/**
	 * 判断是否有子节点
	 */
	private boolean hasChild(List<Depart> list, Depart t) {
		return getChildList(list, t).size() > 0 ? true : false;
	}

	/**
	 * 新增保存部门信息
	 * 
	 * @param depart 部门信息
	 * @return 结果
	 */
	@Override
	public Object insertSelective(Depart depart) {
		if (StrUtils.isNotBlank(checkDeptNameUnique(depart))) {
			throw new ResultException("新增部门'" + depart.getDepartName() + "'失败,部门名称已存在");
		}
		return baseMapper.insertSelective(depart);
	}

	@Override
	public int delete(Long id) {
		if (hasChildByDeptId(id)) {
			throw new ResultException("存在下级部门,不允许删除");
		}
		if (checkDeptExistUser(id)) {
			throw new ResultException("部门存在用户,不允许删除");
		}
		return super.delete(id);
	}

	@Override
	public int deletes(List<Long> ids) {
		int row = 0;
		for (Long id : ids) {
			row += delete(id);
		}
		return row;
	}

	/**
	 * 修改保存部门信息
	 * 
	 * @param depart 部门信息
	 * @return 结果
	 */
	@Override
	public int updateSelective(Depart depart) {
		if (StrUtils.isNotBlank((checkDeptNameUnique(depart)))) {
			throw new ResultException("修改部门'" + depart.getDepartName() + "'失败，部门名称已存在");
		} else if (depart.getPid().equals(depart.getDepartId())) {
			throw new ResultException("修改部门'" + depart.getDepartName() + "'失败，上级部门不能是自己");
		}
		int result = departMapper.updateByPrimaryKeySelective(depart);
		return result;
	}
}