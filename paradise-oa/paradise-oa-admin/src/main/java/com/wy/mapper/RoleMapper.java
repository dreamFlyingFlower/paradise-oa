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
	 * 根据用户编号获取角色列表
	 * 
	 * @param userId 用户编号
	 * @return 用户角色列表
	 */
	List<Role> selectByUserId(Long userId);
}