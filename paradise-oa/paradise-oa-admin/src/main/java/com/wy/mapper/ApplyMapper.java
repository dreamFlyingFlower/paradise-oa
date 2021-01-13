package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Apply;
import com.wy.model.ApplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 请假表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 12:11:22
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface ApplyMapper extends BaseMapper<Apply, Long> {

	long countByExample(ApplyExample example);

	int deleteByExample(ApplyExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Apply record);

	int insertSelective(Apply record);

	List<Apply> selectByExample(ApplyExample example);

	Apply selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Apply record,
			@Param("example") ApplyExample example);

	int updateByExample(@Param("record") Apply record, @Param("example") ApplyExample example);

	int updateByPrimaryKeySelective(Apply record);

	int updateByPrimaryKey(Apply record);
}