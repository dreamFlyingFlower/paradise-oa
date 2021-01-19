package com.wy.mapper;

import java.util.List;

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

	int updateByExampleSelective(@Param("record") Depart record, @Param("example") DepartExample example);

	int updateByExample(@Param("record") Depart record, @Param("example") DepartExample example);

	int updateByPrimaryKeySelective(Depart record);

	int updateByPrimaryKey(Depart record);

	/**
	 * 根据角色编号查询该角色所属部门
	 * 
	 * @param roleId 角色编号
	 * @return 部门集合
	 */
	List<Depart> selectByRoleId(Long roleId);
}