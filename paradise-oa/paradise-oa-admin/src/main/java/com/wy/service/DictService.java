package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.Dict;

/**
 * 系统字典类
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
public interface DictService extends BaseService<Dict, Long> {

	/**
	 * 根据字典编号获得当前字典数据以及直接子级字典数据列表
	 * 
	 * @param dictId 字典编号
	 * @return 当前字典数据以及直接子级字典数据
	 */
	List<Dict> getSelfChildren(long dictId);

	/**
	 * 根据字典唯一标识获得直接子级的字典数据列表
	 * 
	 * @param dictCode 字典唯一标识
	 * @param self 是否获得上级数据,null或false不获取,true获取
	 * @return 直接子级字典数据
	 */
	List<Dict> getChildrenByCode(String dictCode, Boolean self);
}