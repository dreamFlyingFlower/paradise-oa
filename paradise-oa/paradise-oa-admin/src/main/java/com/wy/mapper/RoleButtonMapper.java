package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.RoleButton;
import com.wy.model.RoleButtonExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色按钮中间表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface RoleButtonMapper extends BaseMapper<RoleButton, Long> {

	long countByExample(RoleButtonExample example);

	int deleteByExample(RoleButtonExample example);

	int deleteByPrimaryKey(Long id);

	int insert(RoleButton record);

	int insertSelective(RoleButton record);

	List<RoleButton> selectByExample(RoleButtonExample example);

	RoleButton selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") RoleButton record,
			@Param("example") RoleButtonExample example);

	int updateByExample(@Param("record") RoleButton record, @Param("example") RoleButtonExample example);

	int updateByPrimaryKeySelective(RoleButton record);

	int updateByPrimaryKey(RoleButton record);
}