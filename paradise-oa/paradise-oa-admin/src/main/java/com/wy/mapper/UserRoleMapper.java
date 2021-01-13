package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.UserRole;
import com.wy.model.UserRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色中间表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole, Long> {

	long countByExample(UserRoleExample example);

	int deleteByExample(UserRoleExample example);

	int deleteByPrimaryKey(Long id);

	int insert(UserRole record);

	int insertSelective(UserRole record);

	List<UserRole> selectByExample(UserRoleExample example);

	UserRole selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") UserRole record, @Param("example") UserRoleExample example);

	int updateByExample(@Param("record") UserRole record, @Param("example") UserRoleExample example);

	int updateByPrimaryKeySelective(UserRole record);

	int updateByPrimaryKey(UserRole record);

	/**
	 * 通过用户ID删除用户和角色关联
	 * 
	 * @param userId 用户ID
	 * @return 结果
	 */
	int deleteUserRoleByUserId(Long userId);

	/**
	 * 批量删除用户和角色关联
	 * 
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	int deleteUserRole(Long[] ids);

	/**
	 * 通过角色ID查询角色使用数量
	 * 
	 * @param roleId 角色ID
	 * @return 结果
	 */
	int countUserRoleByRoleId(Long roleId);

	/**
	 * 批量新增用户角色信息
	 * 
	 * @param userRoleList 用户角色列表
	 * @return 结果
	 */
	int batchUserRole(List<UserRole> userRoleList);

	/**
	 * 删除用户和角色关联信息
	 * 
	 * @param userRole 用户和角色关联信息
	 * @return 结果
	 */
	int deleteUserRoleInfo(UserRole userRole);

	/**
	 * 批量取消授权用户角色
	 * 
	 * @param roleId 角色ID
	 * @param userIds 需要删除的用户数据ID
	 * @return 结果
	 */
	int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}