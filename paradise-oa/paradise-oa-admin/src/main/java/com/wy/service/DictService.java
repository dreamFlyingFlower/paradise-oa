package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.Dict;
import com.wy.result.Result;

/**
 * 系统字典类
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
public interface DictService extends BaseService<Dict, Long> {

	/**
	 * 根据字典编号获得当前字典数据以及直接子级字典数据
	 * 
	 * @param dicId 字典编号
	 * @return 当前字典数据以及直接子级字典数据
	 */
	List<Dict> getSelfChildren(long dicId);

	/**
	 * 根据字典唯一标识获得直接子级的字典数据
	 * 
	 * @param dicCode 字典唯一标识
	 * @return 直接子级字典数据
	 */
	List<Dict> getChildren(String dicCode);

	/**
	 * 根据条件分页查询字典数据
	 * 
	 * @param dictData 字典数据信息
	 * @return 字典数据集合信息
	 */
	Result<List<Dict>> selectDictList(Dict dictData);

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
	String selectDictLabel(String dictType, String dictValue);
}