package com.wy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.wy.base.AbstractService;
import com.wy.mapper.DictMapper;
import com.wy.model.Dict;
import com.wy.result.Result;
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

	// /**
	// * 通过字典编号获得字典信息以及直接父级信息
	// */
	// @Override
	// public Object getById(Serializable id) {
	// return absDao.getMap(Sqls
	// .create(StrUtils.formatBuilder(
	// "select a.dic_id dicId,a.dic_name dicName,a.dic_code dicCode,",
	// "a.pid,b.dic_name parentName from td_dic a ",
	// " left join td_dic b on a.pid = b.dic_id where a.dic_id = @id"))
	// .setParam("id", id));
	// }
	//
	/**
	 * 根据父级编号获得父级以及直接子级信息
	 */
	@Override
	public List<Dict> getSelfChildren(long dictId) {
//		return baseMapper.getMaps(
//				Sqls.create(StrUtils.formatBuilder("select dic_id dicId,dic_name dicName,dic_code dicCode from td_dic ",
//						"where pid = @dicId or dic_id = @dicId order by dic_id")).setParam("dicId", dicId));
		return dictMapper.getSelfChildren(dictId);
	}

	@Override
	public List<Dict> getChildren(String dictCode) {
//	 return baseMapper.getMaps(Sqls.create(StrUtils.formatBuilder(
//	 "select dic_id dicId,dic_name dicName,dic_code dicCode from td_dic ",
//	 "where pid in (select dic_id from td_dic where dic_code = @dicCode) order by a.dic_id"))
//	 .setParam("dicCode", dicCode));
		return dictMapper.getChildren(dictCode);
	}
	//
	// /**
	// * 根据父级编号获得直接子级数据
	// * @param id 父级编号
	// * @return 直接子级数据
	// */
	// @Override
	// public List<Map<String, Object>> getTree(int id, boolean parent) {
	// return absDao.getMaps(Sqls.create(StrUtils.formatBuilder(
	// "select b.dic_id treeId,b.dic_name treeName,b.dic_code dicCode,",
	// "(select count(*) from td_dic a where a.pid = b.dic_Id) childNum from td_dic
	// b where ",
	// parent ? " b.pid = @id " : " b.dic_id =@id ", "order by b.dic_id"))
	// .setParam("id", id));
	// }

	/**
	 * 根据条件分页查询字典数据
	 * 
	 * @param dictData 字典数据信息
	 * @return 字典数据集合信息
	 */
	@Override
	public Result<List<Dict>> selectDictList(Dict dict) {
		startPage(dict);
		List<Dict> list = dictMapper.selectEntitys(dict);
		PageInfo<Dict> pageInfo = new PageInfo<>(list);
		return Result.page(list, pageInfo.getPageNum(), pageInfo.getPages(), pageInfo.getTotal());
	}

	/**
	 * 根据字典类型查询字典数据
	 * 
	 * @param dictType 字典类型
	 * @return 字典数据集合信息
	 */
	@Override
	public List<Dict> selectDictDataByType(String dictType) {
		return dictMapper.selectDictDataByType(dictType);
	}

	/**
	 * 根据字典类型和字典键值查询字典数据信息
	 * 
	 * @param dictType 字典类型
	 * @param dictValue 字典键值
	 * @return 字典标签
	 */
	@Override
	public String selectDictLabel(String dictType, String dictValue) {
		return dictMapper.selectDictLabel(dictType, dictValue);
	}
}