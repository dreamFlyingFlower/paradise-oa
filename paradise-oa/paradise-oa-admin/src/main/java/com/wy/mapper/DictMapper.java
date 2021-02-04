package com.wy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wy.base.BaseMapper;
import com.wy.model.Dict;
import com.wy.model.DictExample;

/**
 * 系统字典类
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict, Long> {

	long countByExample(DictExample example);

	int deleteByExample(DictExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Dict record);

	int insertSelective(Dict record);

	List<Dict> selectByExample(DictExample example);

	Dict selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Dict record, @Param("example") DictExample example);

	int updateByExample(@Param("record") Dict record, @Param("example") DictExample example);

	int updateByPrimaryKeySelective(Dict record);

	int updateByPrimaryKey(Dict record);

	/**
	 * 根据字典唯一标识获得直接子级的字典数据
	 * 
	 * @param dicCode 字典唯一标识
	 * @return 直接子级字典数据
	 */
	List<Dict> selectChildrenByCode(String dictCode, Boolean self);
}