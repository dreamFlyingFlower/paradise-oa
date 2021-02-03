package com.wy.base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.wy.database.Sort;
import com.wy.database.Unique;
import com.wy.result.ResultException;
import com.wy.utils.StrUtils;

/**
 * 基础service层,通用service的DML方法
 * 
 * @author ParadiseWY
 * @date 2019-08-05 15:51:27
 * @git {@link https://github.com/mygodness100}
 */
public abstract class AbstractService<T, ID> extends AbstractQueryService<T, ID> implements BaseService<T, ID> {

	@Override
	@Transactional
	public Object insert(T t) {
		handlerUniqueAndSort(t, true, true);
		return baseMapper.insert(t);
	}

	@Override
	@Transactional
	public Object insertSelective(T t) {
		handlerUniqueAndSort(t, true, true);
		return baseMapper.insertSelective(t);
	}

	@Override
	@Transactional
	public Object inserts(List<T> ts) {
		baseMapper.inserts(ts);
		return ts;
	}

	/**
	 * 检查实体类中需要进行唯一性校验和排序的字段
	 * 
	 * @param model 实体类
	 * @param saveOrUpdate 新增或更新
	 * @param checkSort 是否检查排序,新增的时候默认检查,修改的时候不检查
	 */
	private void handlerUniqueAndSort(T model, boolean saveOrUpdate, boolean checkSort) {
		Field[] fields = clazz.getDeclaredFields();
		// Map<String, Object> map = new HashMap<>();
		for (Field field : fields) {
			// 检查是否有unique字段
			if (field.isAnnotationPresent(Unique.class)) {
				field.setAccessible(true);
				try {
					if (saveOrUpdate) {
						// 当新增时可以直接加入检查唯一的map中,不可多字段同时检查
						Map<String, Object> temp = new HashMap<>();
						temp.put(field.getName(), field.get(model));
						if (hasValue(JSON.parseObject(JSON.toJSONString(temp), clazz))) {
							throw new ResultException("参数有重复值,请检查");
						}
					} else {
						// 当更新时,检查原始值和新值是否相同,若相同,不用再查数据库,且需要将实体类中的该字段值置空
						Unique unique = field.getAnnotation(Unique.class);
						String oriName = unique.oriName();
						if (StrUtils.isBlank(oriName)) {
							oriName = "ori" + StrUtils.upperFirst(field.getName());
						}
						Field actualField = clazz.getDeclaredField(oriName);
						actualField.setAccessible(true);
						Object object = actualField.get(model);
						if (Objects.equals(object, field.get(model))) {
							field.set(model, null);
						} else {
							Map<String, Object> temp = new HashMap<>();
							temp.put(field.getName(), field.get(model));
							if (hasValue(JSON.parseObject(JSON.toJSONString(temp), clazz))) {
								throw new ResultException("参数有重复值,请检查");
							}
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					e.printStackTrace();
				}
			}
			// 检查是否有排序字段
			if (checkSort && field.isAnnotationPresent(Sort.class)) {
				Long maxSort = validSort(field, model);
				if (maxSort == null) {
					maxSort = 0l;
				}
				if (maxSort == -1) {
					throw new ResultException("排序字段错误");
				} else {
					try {
						field.set(model, (int) maxSort.longValue() + 1);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
						throw new ResultException("新增系统错误");
					}
				}
			}
		}
	}

	private Long validSort(Field field, T model) {
		field.setAccessible(true);
		try {
			Object value = field.get(model);
			if (Objects.nonNull(value)) {
				Long number = NumberUtils.toLong(field.get(model).toString());
				if (number > 0) {
					return number;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return -1l;
		}
		Sort sort = field.getAnnotation(Sort.class);
		return baseMapper.getMaxValue(StrUtils.isBlank(sort.value()) ? field.getName() : sort.value());
	}

	@Override
	@Transactional
	public int delete(ID id) {
		return baseMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional
	public int deletes(List<ID> ids) {
		return baseMapper.deleteByPrimaryKeys(ids);
	}

	@Override
	@Transactional
	public int clear() {
		return baseMapper.deleteAll();
	}

	/**
	 * 更新,若实体类中有{@link Unique}标识字段,更新时需要传入该字段的原始值
	 * 
	 * 如:原字段为username,更新时需要传入oriUsername的值,该值必传,否则更新失败
	 */
	@Override
	@Transactional
	public int update(T t) {
		handlerUniqueAndSort(t, false, false);
		return baseMapper.updateByPrimaryKey(t);
	}

	/**
	 * 更新,若实体类中有{@link Unique}标识字段,更新时需要传入该字段的原始值
	 * 
	 * 如:原字段为username,更新时需要传入oriUsername的值,该值必传,否则更新失败
	 */
	@Override
	@Transactional
	public int updateSelective(T t) {
		handlerUniqueAndSort(t, false, false);
		return baseMapper.updateByPrimaryKeySelective(t);
	}
}