package com.wy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wy.base.BaseMapper;
import com.wy.model.Menu;
import com.wy.model.MenuExample;

/**
 * 菜单表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu, Long> {

	long countByExample(MenuExample example);

	int deleteByExample(MenuExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Menu record);

	int insertSelective(Menu record);

	List<Menu> selectByExample(MenuExample example);

	Menu selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Menu record,
			@Param("example") MenuExample example);

	int updateByExample(@Param("record") Menu record, @Param("example") MenuExample example);

	int updateByPrimaryKeySelective(Menu record);

	int updateByPrimaryKey(Menu record);
	
	/**
	 * 根据用户所有权限
	 * 
	 * @return 权限列表
	 */
	List<String> selectMenuPerms();

	/**
	 * 根据用户查询系统菜单列表
	 * 
	 * @param userId 用户编号
	 * @return 菜单列表
	 */
	List<Menu> selectByUserId(Long userId);

	/**
	 * 根据用户ID查询权限
	 * 
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	List<String> selectMenuPermsByUserId(Long userId);

	/**
	 * 根据角色ID查询菜单树信息
	 * 
	 * @param roleId 角色ID
	 * @return 选中菜单列表
	 */
	List<Integer> selectMenuListByRoleId(Long roleId);
}