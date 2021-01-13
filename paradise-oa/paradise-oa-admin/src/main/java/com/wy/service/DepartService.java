package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.base.Tree;
import com.wy.model.Depart;

/**
 * 部门表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
public interface DepartService extends BaseService<Depart, Long> {

	/**
	 * 构建前端所需要树结构
	 * 
	 * @param depts 部门列表
	 * @return 树结构列表
	 */
	List<Depart> buildDeptTree(List<Depart> depts);

	/**
	 * 构建前端所需要下拉树结构
	 * 
	 * @param depts 部门列表
	 * @return 下拉树结构列表
	 */
	List<Tree<Depart, Long>> buildDeptTreeSelect(List<Depart> depts);

	/**
	 * 根据角色ID查询部门树信息
	 * 
	 * @param roleId 角色ID
	 * @return 选中部门列表
	 */
	List<Integer> selectDeptListByRoleId(Long roleId);

	/**
	 * 是否存在部门子节点
	 * 
	 * @param deptId 部门ID
	 * @return 结果
	 */
	boolean hasChildByDeptId(Long deptId);

	/**
	 * 查询部门是否存在用户
	 * 
	 * @param deptId 部门ID
	 * @return 结果 true 存在 false 不存在
	 */
	boolean checkDeptExistUser(Long deptId);

	/**
	 * 校验部门名称是否唯一
	 * 
	 * @param dept 部门信息
	 * @return 结果
	 */
	String checkDeptNameUnique(Depart dept);
}