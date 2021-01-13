package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Role;
import com.wy.model.RoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role, Long> {

	long countByExample(RoleExample example);

	int deleteByExample(RoleExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Role record);

	int insertSelective(Role record);

	List<Role> selectByExample(RoleExample example);

	Role selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

	int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

	int updateByPrimaryKeySelective(Role record);

	int updateByPrimaryKey(Role record);

	/**
	 * 根据用户ID查询角色
	 * 
	 * @param userId 用户ID
	 * @return 角色列表
	 */
	List<Role> selectRolePermissionByUserId(Long userId);

	/**
	 * 根据用户ID获取角色选择框列表
	 * 
	 * @param userId 用户ID
	 * @return 选中角色ID列表
	 */
	List<Long> selectRoleListByUserId(Long userId);

	/**
	 * 根据用户ID查询角色
	 * 
	 * @param userName 用户名
	 * @return 角色列表
	 */
	List<Role> selectRolesByUserName(String userName);

	/**
	 * 校验角色名称是否唯一
	 * 
	 * @param roleName 角色名称
	 * @return 角色信息
	 */
	Role checkRoleNameUnique(String roleName);

	/**
	 * 校验角色权限是否唯一
	 * 
	 * @param roleKey 角色权限
	 * @return 角色信息
	 */
	Role checkRoleKeyUnique(String roleKey);
}