package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Button;
import com.wy.model.ButtonExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 菜单按钮表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface ButtonMapper extends BaseMapper<Button, Long> {

	long countByExample(ButtonExample example);

	int deleteByExample(ButtonExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Button record);

	int insertSelective(Button record);

	List<Button> selectByExample(ButtonExample example);

	Button selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Button record,
			@Param("example") ButtonExample example);

	int updateByExample(@Param("record") Button record, @Param("example") ButtonExample example);

	int updateByPrimaryKeySelective(Button record);

	int updateByPrimaryKey(Button record);
}