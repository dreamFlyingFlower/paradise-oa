package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.RoleDepart;
import com.wy.model.RoleDepartExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色部门关联表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 11:01:35
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface RoleDepartMapper extends BaseMapper<RoleDepart, Long> {

	long countByExample(RoleDepartExample example);

	int deleteByExample(RoleDepartExample example);

	int deleteByPrimaryKey(Long id);

	int insert(RoleDepart record);

	int insertSelective(RoleDepart record);

	List<RoleDepart> selectByExample(RoleDepartExample example);

	RoleDepart selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") RoleDepart record, @Param("example") RoleDepartExample example);

	int updateByExample(@Param("record") RoleDepart record, @Param("example") RoleDepartExample example);

	int updateByPrimaryKeySelective(RoleDepart record);

	int updateByPrimaryKey(RoleDepart record);

	/**
	 * 通过角色ID删除角色和部门关联
	 * 
	 * @param roleId 角色ID
	 * @return 结果
	 */
	int deleteRoleDeptByRoleId(Long roleId);

	/**
	 * 批量删除角色部门关联信息
	 * 
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	int deleteRoleDept(Long[] ids);

	/**
	 * 查询部门使用数量
	 * 
	 * @param deptId 部门ID
	 * @return 结果
	 */
	int selectCountRoleDeptByDeptId(Long deptId);

	/**
	 * 批量新增角色部门信息
	 * 
	 * @param roleDeptList 角色部门列表
	 * @return 结果
	 */
	int batchRoleDept(List<RoleDepart> roleDeptList);
}