package com.wy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import com.wy.base.BaseMapper;
import com.wy.model.Menu;
import com.wy.model.MenuExample;
import com.wy.model.vo.PermissionVo;

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

	int updateByExampleSelective(@Param("record") Menu record, @Param("example") MenuExample example);

	int updateByExample(@Param("record") Menu record, @Param("example") MenuExample example);

	int updateByPrimaryKeySelective(Menu record);

	int updateByPrimaryKey(Menu record);

	/**
	 * 根据用户查询系统菜单列表
	 * 
	 * @param userId 用户编号
	 * @return 菜单列表
	 */
	List<Menu> selectByUserId(Long userId);

	/**
	 * 根据用户ID查询权限select distinct m.perms from ti_menu m left join tr_role_menu rm on m.menu_id =
	 * rm.menu_id left join tr_user_role ur on rm.role_id = ur.role_id where ur.user_id = #{userId}
	 * 
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	List<String> selectPermsByUserId(Long userId);

	/**
	 * 根据用户编号或角色编号获得获得菜单以及菜单权限
	 * 
	 * @param menuId 菜单编号
	 * @param pid 上级菜单编号
	 * @param userId 用户编号
	 * @param roleId 角色编号
	 * @return 菜单以及权限列表
	 */
	List<PermissionVo> selectPermissions(@Nullable Long menuId, @Nullable Long pid, @Nullable Long userId,
			@Nullable Long roleId);
}