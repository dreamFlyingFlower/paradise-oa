package com.wy.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * reidst工具类
 *
 * @author ParadiseWY
 * @date 2020-04-08 12:35:36
 */
@Component
public class RedisService {

	@Autowired
	public RedisTemplate<Object, Object> redisTemplate;

	/**
	 * 缓存基本对象
	 * 
	 * @param key 缓存的键值
	 * @param value 缓存的值
	 * @return 缓存的对象
	 */
	public ValueOperations<Object, Object> setObject(String key, Object value) {
		ValueOperations<Object, Object> operation = redisTemplate.opsForValue();
		operation.set(key, value);
		return operation;
	}

	/**
	 * 缓存基本对象
	 * 
	 * @param key 缓存的键值
	 * @param value 缓存的值
	 * @param timeout 时间
	 * @param timeUnit 时间颗粒度
	 * @return 缓存的对象
	 */
	public ValueOperations<Object, Object> setObject(String key, Object value, Integer timeout, TimeUnit timeUnit) {
		ValueOperations<Object, Object> operation = redisTemplate.opsForValue();
		operation.set(key, value, timeout, timeUnit);
		return operation;
	}

	/**
	 * 获得缓存的基本对象。
	 * 
	 * @param key 缓存键值
	 * @return 缓存键值对应的数据
	 */
	public Object getObject(String key) {
		ValueOperations<Object, Object> operation = redisTemplate.opsForValue();
		return operation.get(key);
	}

	/**
	 * 删除单个对象
	 * 
	 * @param key
	 */
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 删除集合对象
	 * 
	 * @param collection
	 */
	public void delete(Collection<Object> collection) {
		redisTemplate.delete(collection);
	}

	/**
	 * 缓存List数据
	 * 
	 * @param key 缓存的键值
	 * @param dataList 待缓存的List数据
	 * @return 缓存的对象
	 */
	public ListOperations<Object, Object> setList(String key, List<Object> dataList) {
		ListOperations<Object, Object> listOperation = redisTemplate.opsForList();
		if (null != dataList) {
			int size = dataList.size();
			for (int i = 0; i < size; i++) {
				listOperation.leftPush(key, dataList.get(i));
			}
		}
		return listOperation;
	}

	/**
	 * 获得缓存的list对象
	 * 
	 * @param key 缓存的键值
	 * @return 缓存键值对应的数据
	 */
	public List<Object> getList(String key) {
		List<Object> dataList = new ArrayList<Object>();
		ListOperations<Object, Object> listOperation = redisTemplate.opsForList();
		Long size = listOperation.size(key);
		for (int i = 0; i < size; i++) {
			dataList.add(listOperation.index(key, i));
		}
		return dataList;
	}

	/**
	 * 缓存Set
	 * 
	 * @param key 缓存键值
	 * @param dataSet 缓存的数据
	 * @return 缓存数据的对象
	 */
	public BoundSetOperations<Object, Object> setSet(String key, Set<Object> dataSet) {
		BoundSetOperations<Object, Object> setOperation = redisTemplate.boundSetOps(key);
		Iterator<Object> it = dataSet.iterator();
		while (it.hasNext()) {
			setOperation.add(it.next());
		}
		return setOperation;
	}

	/**
	 * 获得缓存的set
	 * 
	 * @param key
	 * @return
	 */
	public Set<Object> getSet(String key) {
		Set<Object> dataSet = new HashSet<Object>();
		BoundSetOperations<Object, Object> operation = redisTemplate.boundSetOps(key);
		dataSet = operation.members();
		return dataSet;
	}

	/**
	 * 缓存Map
	 * 
	 * @param key
	 * @param dataMap
	 * @return
	 */
	public HashOperations<Object, Object, Object> setMap(String key, Map<Object, Object> dataMap) {
		HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
		if (null != dataMap) {
			for (Map.Entry<Object, Object> entry : dataMap.entrySet()) {
				hashOperations.put(key, entry.getKey(), entry.getValue());
			}
		}
		return hashOperations;
	}

	/**
	 * 获得缓存的Map
	 * 
	 * @param key
	 * @return
	 */
	public Map<Object, Object> getMap(String key) {
		Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
		return map;
	}

	/**
	 * 获得缓存的基本对象列表
	 * 
	 * @param pattern 字符串前缀
	 * @return 对象列表
	 */
	public Collection<Object> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}
}