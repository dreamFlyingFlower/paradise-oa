package com.wy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wy.base.BaseMapper;
import com.wy.model.RoleMenu;
import com.wy.model.RoleMenuExample;

/**
 * 角色菜单表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu, Long> {

	long countByExample(RoleMenuExample example);

	int deleteByExample(RoleMenuExample example);

	int deleteByPrimaryKey(Long id);

	int insert(RoleMenu record);

	int insertSelective(RoleMenu record);

	List<RoleMenu> selectByExample(RoleMenuExample example);

	RoleMenu selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") RoleMenu record, @Param("example") RoleMenuExample example);

	int updateByExample(@Param("record") RoleMenu record, @Param("example") RoleMenuExample example);

	int updateByPrimaryKeySelective(RoleMenu record);

	int updateByPrimaryKey(RoleMenu record);
}