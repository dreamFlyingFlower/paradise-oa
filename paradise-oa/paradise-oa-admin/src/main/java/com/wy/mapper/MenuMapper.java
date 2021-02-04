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

	int updateByExampleSelective(@Param("record") Menu record, @Param("example") MenuExample example);

	int updateByExample(@Param("record") Menu record, @Param("example") MenuExample example);

	int updateByPrimaryKeySelective(Menu record);

	int updateByPrimaryKey(Menu record);

	/**
	 * 根据角色id查询菜单列表
	 * 
	 * @param roleId 角色id
	 * @return 菜单列表
	 */
	List<Menu> selectByRoleId(Long roleId);
}