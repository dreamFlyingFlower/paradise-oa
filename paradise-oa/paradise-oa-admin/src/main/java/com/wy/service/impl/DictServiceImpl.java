package com.wy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.mapper.DictMapper;
import com.wy.model.Dict;
import com.wy.model.DictExample;
import com.wy.service.DictService;

/**
 * 系统字典类
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Service("dictService")
public class DictServiceImpl extends AbstractService<Dict, Long> implements DictService {

	@Autowired
	private DictMapper dictMapper;

	@Override
	public List<Dict> getSelfChildren(long dictId) {
		DictExample dictExample = new DictExample();
		dictExample.or().andDictIdEqualTo(dictId);
		dictExample.or().andPidEqualTo(dictId);
		return dictMapper.selectByExample(dictExample);
	}

	@Override
	public List<Dict> getChildrenByCode(String dictCode, Boolean self) {
		return dictMapper.selectChildrenByCode(dictCode, self);
	}
}