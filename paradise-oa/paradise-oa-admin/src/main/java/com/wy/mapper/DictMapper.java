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
	 * 根据字典编号获得当前字典数据以及直接子级字典数据
	 * 
	 * @param dicId 字典编号
	 * @return 当前字典数据以及直接子级字典数据
	 */
	List<Dict> getSelfChildren(long dictId);

	/**
	 * 根据字典唯一标识获得直接子级的字典数据
	 * 
	 * @param dicCode 字典唯一标识
	 * @return 直接子级字典数据
	 */
	List<Dict> getChildren(String dictCode);

	/**
	 * 根据字典类型查询字典数据
	 * 
	 * @param dictType 字典类型
	 * @return 字典数据集合信息
	 */
	List<Dict> selectDictDataByType(String dictType);

	/**
	 * 根据字典类型和字典键值查询字典数据信息
	 * 
	 * @param dictType 字典类型
	 * @param dictValue 字典键值
	 * @return 字典标签
	 */
	String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

	/**
	 * 查询字典数据
	 * 
	 * @param dictType 字典类型
	 * @return 字典数据
	 */
	int countDictDataByType(String dictType);

	/**
	 * 同步修改字典类型
	 * 
	 * @param oldDictType 旧字典类型
	 * @param newDictType 新旧字典类型
	 * @return 结果
	 */
	int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);
}