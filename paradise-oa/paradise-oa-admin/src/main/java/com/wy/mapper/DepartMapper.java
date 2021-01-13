package com.wy.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wy.base.BaseMapper;
import com.wy.model.Depart;
import com.wy.model.DepartExample;

/**
 * 部门表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface DepartMapper extends BaseMapper<Depart, Long> {

	long countByExample(DepartExample example);

	int deleteByExample(DepartExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Depart record);

	int insertSelective(Depart record);

	List<Depart> selectByExample(DepartExample example);

	Depart selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Depart record,
			@Param("example") DepartExample example);

	int updateByExample(@Param("record") Depart record, @Param("example") DepartExample example);

	int updateByPrimaryKeySelective(Depart record);

	int updateByPrimaryKey(Depart record);
	
	/**
	 * @apiNote 根据id查询信息,包括上级信息和本级信息
	 * @param id 本级部门编号
	 * @return 上级和本级信息
	 */
	List<Map<String, Object>> getTreeById(Long id);

	/**
	 * @apiNote 根据上级id查询下级部门信息
	 * @param id 上级部门编号
	 * @return 下级信息
	 */
	List<Map<String, Object>> getTreeByPid(Long id);

	/**
	 * 根据角色ID查询部门树信息
	 * 
	 * @param roleId 角色ID
	 * @return 选中部门列表
	 */
	List<Integer> selectDeptListByRoleId(Long roleId);

	/**
	 * 根据ID查询所有子部门
	 * 
	 * @param deptId 部门ID
	 * @return 部门列表
	 */
	List<Depart> selectChildrenDeptById(Long deptId);

	/**
	 * 是否存在子节点
	 * 
	 * @param deptId 部门ID
	 * @return 结果
	 */
	int hasChildByDeptId(Long deptId);

	/**
	 * 查询部门是否存在用户
	 * 
	 * @param deptId 部门ID
	 * @return 结果
	 */
	int checkDeptExistUser(Long deptId);

	/**
	 * 校验部门名称是否唯一
	 * 
	 * @param deptName 部门名称
	 * @param parentId 父部门ID
	 * @return 结果
	 */
	Depart checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

	/**
	 * 修改所在部门的父级部门状态
	 * 
	 * @param dept 部门
	 */
	void updateDeptStatus(Depart dept);

	/**
	 * 修改子元素关系
	 * 
	 * @param depts 子元素
	 * @return 结果
	 */
	int updateDeptChildren(@Param("depts") List<Depart> depts);
}