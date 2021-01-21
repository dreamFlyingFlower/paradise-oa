package com.wy.base;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wy.database.annotation.Pid;
import com.wy.database.annotation.Pri;
import com.wy.enums.TipEnum;
import com.wy.excel.ExcelModelUtils;
import com.wy.result.Result;
import com.wy.result.ResultException;
import com.wy.utils.ListUtils;
import com.wy.utils.MapUtils;
import com.wy.utils.StrUtils;

/**
 * 基础service层,通用service查询方法
 * 
 * 所有继承该类的子类的泛型都不可相同,即不可有2个子类的泛型相同,否则启动报错;可不使用baseMapper
 * 
 * @author ParadiseWY
 * @date 2019-08-05 15:51:27
 * @git {@link https://github.com/mygodness100}
 */
@SuppressWarnings("unchecked")
public abstract class AbstractQueryService<T, ID> implements BaseService<T, ID> {

	@Autowired
	public BaseMapper<T, ID> baseMapper;

	/** 有线程安全问题 FIXME */
	public Class<T> clazz;

	/**
	 * 获得子类的泛型class
	 */
	public AbstractQueryService() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		Type type = parameterizedType.getActualTypeArguments()[0];
		clazz = (Class<T>) type;
	}

	/**
	 * 获得子类的泛型class
	 */
	public Class<T> getClassOfT() {
		return clazz;
	}

	/**
	 * 利用pagehelper分页,也可以在xml中手动写limit
	 * 
	 * @param pager 分页参数
	 * @return 分页信息
	 */
	public Page<Object> startPage(AbstractPager pager) {
		String pageDirection = pager.getPageDirection();
		if (pager.hasPager()) {
			if (StrUtils.isNotBlank(pager.getPageOrder())) {
				return PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(),
						pager.getPageOrder() + " " + (StrUtils.isBlank(pageDirection) ? " desc " : pageDirection));
			} else {
				return PageHelper.startPage(pager.getPageIndex(), pager.getPageSize());
			}
		}
		return null;
	}

	public Page<Object> startPage(Integer pageIndex, Integer pageSize) {
		if (pageIndex != null && pageIndex > 0) {
			if (pageSize == null || pageSize <= 0) {
				pageSize = 10;
			}
			return PageHelper.startPage(pageIndex, pageSize);
		}
		return null;
	}

	@Override
	public boolean hasValue(T t) {
		return baseMapper.countByEntity(t) > 0;
	}

	@Override
	public Object getById(ID id) {
		return baseMapper.selectByPrimaryKey(id);
	}

	/**
	 * 一次性查询表中所有数据处理后形成树形结构数据,不适合特别大的数据,若数据超大,可使用{@link #getRecursionTree}
	 * 
	 * @param id 查询条件
	 * @param self 是否查询本级数据,true是,false直接获取下级,默认false
	 * @param params 其他基本类型参数
	 * @return 树形结果集
	 */
	@Override
	public List<T> getTree(ID id, Boolean self, Map<String, Object> params) {
		// 获得主键和直接下级的map
		Map<ID, List<T>> treeMaps = getLeaf(params);
		self = self == null ? false : self.booleanValue();
		// 查询本级或直接下级数据
		List<T> trees = self ? new ArrayList<>(Arrays.asList(baseMapper.selectByPrimaryKey(id))) : treeMaps.get(id);
		// 递归查询下级数据
		getLeaf(trees, treeMaps);
		return trees;
	}

	/**
	 * 根据id将数据进行匹配,可重写
	 * 
	 * @param params 其他基本类型参数
	 * @return 匹配结果
	 */
	@Override
	public Map<ID, List<T>> getLeaf(Map<String, Object> params) {
		Field[] declaredFields = clazz.getDeclaredFields();
		if (ArrayUtils.isEmpty(declaredFields)) {
			throw new ResultException("this class does not have nothing");
		}
		Map<String, Field> priAndPidField = handlerPriAndPidField(declaredFields);
		List<T> entitys = getTreeAll(params, priAndPidField);
		if (ListUtils.isBlank(entitys)) {
			return null;
		}
		Field priField = priAndPidField.get("priField");
		Field pidField = priAndPidField.get("pidField");
		if (Objects.isNull(priField) && Objects.isNull(pidField)) {
			throw new ResultException("this class does not have primary key or does not have pid");
		}
		return handlerTreeMap(entitys, priField, pidField);
	}

	/**
	 * 获得所有符合查询条件的数据集合,规定所有的根数据的pid为0
	 * 
	 * @param params 其他基本类型参数
	 * @param priAndPidField 主键字段和上级字段
	 * @return 数据集合
	 */
	public List<T> getTreeAll(Map<String, Object> params, Map<String, Field> priAndPidField) {
		List<T> entitys = null;
		if (MapUtils.isBlank(params)) {
			entitys = baseMapper.selectEntitys(null);
		} else {
			T param = JSON.parseObject(JSON.toJSONString(params), clazz);
			entitys = baseMapper.selectEntitys(param);
		}
		// 添加根数据,不在数据库中存在,如字典表,所有字典的pid为0,数据库中没有,需要手动添加到其中
		entitys.add(JSON.parseObject(JSON.toJSONString(
				MapUtils.builder("treeId", 0).put("treeName", "ROOT").put(priAndPidField.get("priField").getName(), 0)
						.put(priAndPidField.get("pidField").getName(), -1).build()),
				clazz));
		return entitys;
	}

	/**
	 * 获得本级主键字段和上级字段
	 * 
	 * @param declaredFields 所有字段
	 * @return 本级主键字段和上级字段
	 */
	private Map<String, Field> handlerPriAndPidField(Field[] declaredFields) {
		Map<String, Field> priAndPid = new HashMap<>(4);
		for (Field field : declaredFields) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(Pri.class)) {
				priAndPid.put("priField", field);
			}
			if (field.isAnnotationPresent(Pid.class)) {
				priAndPid.put("pidField", field);
			}
			if (Objects.nonNull(priAndPid.get("priField")) && Objects.nonNull(priAndPid.get("pidField"))) {
				break;
			}
		}
		return priAndPid;
	}

	/**
	 * 处理本级和下级数据,循环次数过多,可以再优化 FIXME
	 * 
	 * @param entitys 全部数据
	 * @param priField 主键字段
	 * @param pidField 上级字段
	 * @return 上下级字段对应关系
	 */
	private Map<ID, List<T>> handlerTreeMap(List<T> entitys, Field priField, Field pidField) {
		Map<ID, List<T>> result = new HashMap<>(entitys.size());
		for (T t : entitys) {
			priField.setAccessible(true);
			try {
				// 检查是否存在本级集合数据
				ID priVal = (ID) priField.get(t);
				List<T> priList = result.get(priVal);
				if (ListUtils.isBlank(priList)) {
					priList = new ArrayList<>();
					result.put(priVal, priList);
				}
				for (T t1 : entitys) {
					// 将本级的下级实例放入集合中
					if ((priVal instanceof Number && priVal == pidField.get(t1))
							|| (priVal.getClass() == String.class && priVal.equals(pidField.get(t1)))) {
						priList.add(t1);
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 递归获得符合的数据
	 * 
	 * @param trees 最终数据
	 * @param params 全部符合的数据
	 */
	public void getLeaf(List<T> trees, Map<ID, List<T>> params) {
		if (ListUtils.isBlank(trees)) {
			return;
		}
		for (T t : trees) {
			if (!(t instanceof Tree)) {
				throw new ResultException("this class does not extends Tree");
			}
			Tree<T, ID> tree = (Tree<T, ID>) t;
			if (ListUtils.isNotBlank(params.get(tree.getTreeId()))) {
				getLeaf(params.get(tree.getTreeId()), params);
				tree.setChildren(params.get(tree.getTreeId()));
			}
		}
	}

	/**
	 * 递归查询表中树形结构,需要重写getChildren方法.
	 * 
	 * @param id 查询条件
	 * @param self 是否查询本级数据,true获取,false直接获取下级,默认false
	 * @param params 其他基本类型参数
	 * @return 树形结果集
	 */
	public List<T> getRecursionTree(ID id, Boolean self, Map<String, Object> params) {
		List<T> trees = getRecursionLeaf(id, self == null ? false : self.booleanValue(), params);
		getRecursionLeaf(trees, params);
		return trees;
	}

	/**
	 * 该方法根据上级编号查询本级数据或下级数据.为统一前端树形结构,需要将标识符,
	 * 如id全部转为treeId,显示的名称都改为treeName.同时每次查询都需要将下级的数量查询出来,放入childNum字段中 {@link select b.dic_id
	 * treeId,b.dic_name treeName,b.dic_code dicCode, (select count(*) from td_dic a where a.pid =
	 * b.dic_Id) childNum from td_dic b}
	 * 
	 * @param id 条件编号
	 * @param self 是否查询本级数据,true获取,false直接获取下级
	 * @param params 其他基本类型参数
	 * @return 树形结果集
	 */
	public List<T> getRecursionLeaf(ID id, boolean self, Map<String, Object> params) {
		return null;
	}

	public void getRecursionLeaf(List<T> trees, Map<String, Object> params) {
		if (ListUtils.isBlank(trees)) {
			return;
		}
		for (T t : trees) {
			if (!(t instanceof Tree)) {
				throw new ResultException("this class does not extends Tree");
			}
			Tree<T, ID> tree = (Tree<T, ID>) t;
			if (tree.getChildNum() > 0) {
				List<T> childs = getRecursionLeaf(tree.getTreeId(), false, params);
				getRecursionLeaf(childs, params);
				tree.setChildren(childs);
			}
		}
	}

	@Override
	public Result<List<T>> getEntitys(T t) {
		if (t != null && t instanceof AbstractPager) {
			startPage((AbstractPager) t);
			List<T> listByEntitys = baseMapper.selectEntitys(t);
			PageInfo<T> pageInfo = new PageInfo<>(listByEntitys);
			return Result.page(listByEntitys, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
		}
		return Result.ok(baseMapper.selectEntitys(t));
	}

	@Override
	public void getExport(T t, HttpServletRequest request, HttpServletResponse response) {
		Result<List<T>> entitys = getEntitys(t);
		String excelName = StrUtils.isBlank(request.getParameter("excelName")) ? "数据导出"
				: request.getParameter("excelName");
		ExcelModelUtils.getInstance().exportExcel(entitys.getData(), response, excelName);
	}

	@Override
	public Result<List<Map<String, Object>>> getLists(Map<String, Object> params) {
		if (params == null) {
			return Result.ok(baseMapper.selectLists(new HashMap<>()));
		}
		if (params.get("pageIndex") == null || NumberUtils.toInt(params.get("pageIndex").toString()) < 0) {
			return Result.ok(baseMapper.selectLists(params));
		}
		int pageSize = 0;
		if (params.get("pageSize") == null || NumberUtils.toInt(params.get("pageSize").toString()) <= 0) {
			pageSize = 10;
		} else {
			pageSize = NumberUtils.toInt(params.get("pageSize").toString());
		}
		PageHelper.startPage(NumberUtils.toInt(params.get("pageIndex").toString()), pageSize);
		List<Map<String, Object>> lists = baseMapper.selectLists(params);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(lists);
		return Result.page(lists, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
	}

	@Override
	public Object insert(T t) {
		throw new ResultException(TipEnum.TIP_AUTH_DENIED);
	}

	@Override
	public Object insertSelective(T t) {
		throw new ResultException(TipEnum.TIP_AUTH_DENIED);
	}

	@Override
	public Object inserts(List<T> ts) {
		throw new ResultException(TipEnum.TIP_AUTH_DENIED);
	}

	@Override
	public int delete(ID id) {
		throw new ResultException(TipEnum.TIP_AUTH_DENIED);
	}

	@Override
	public int deletes(List<ID> ids) {
		throw new ResultException(TipEnum.TIP_AUTH_DENIED);
	}

	@Override
	public int clear() {
		throw new ResultException(TipEnum.TIP_AUTH_DENIED);
	}

	@Override
	public int update(T t) {
		throw new ResultException(TipEnum.TIP_AUTH_DENIED);
	}

	@Override
	public int updateSelective(T t) {
		throw new ResultException(TipEnum.TIP_AUTH_DENIED);
	}
}