package com.wy.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;

/**
 * @apiNote redis自定义配置,使用fastjson序列化
 * @author ParadiseWY
 * @date 2020年4月2日 下午4:26:04
 */
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

	/**
	 * 对象,map等数据类型在redis中的存储需要用到fastjson进行序列化,也可使用spring的依赖jackson
	 */
	@Bean
	@ConditionalOnMissingBean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();

		// 使用fastjson序列化
		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
		// 设置方法参照{RedisTemplate}类中的afterPropertiesSet方法
		// value值的序列化采用fastJsonRedisSerializer
		template.setValueSerializer(fastJsonRedisSerializer);
		template.setHashValueSerializer(fastJsonRedisSerializer);
		// key的序列化采用StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(redisConnectionFactory);
		init(template);
		return template;
	}

	public void init(RedisTemplate<Object, Object> redisTemplate) {
		initVal(redisTemplate);
		initList(redisTemplate);
		initSet(redisTemplate);
		initHashMap(redisTemplate);
		initCluster(redisTemplate);
		initGeo(redisTemplate);
		initHyperLogLog(redisTemplate);
		initZSet(redisTemplate);
	}

	@Bean
	public ValueOperations<Object, Object> initVal(RedisTemplate<Object, Object> redisTemplate) {
		return redisTemplate.opsForValue();
	}

	@Bean
	public ListOperations<Object, Object> initList(RedisTemplate<Object, Object> redisTemplate) {
		return redisTemplate.opsForList();
	}

	@Bean
	public SetOperations<Object, Object> initSet(RedisTemplate<Object, Object> redisTemplate) {
		return redisTemplate.opsForSet();
	}

	@Bean
	public HashOperations<Object, Object, Object> initHashMap(RedisTemplate<Object, Object> redisTemplate) {
		return redisTemplate.opsForHash();
	}

	@Bean
	public ClusterOperations<Object, Object> initCluster(RedisTemplate<Object, Object> redisTemplate) {
		return redisTemplate.opsForCluster();
	}

	@Bean
	public GeoOperations<Object, Object> initGeo(RedisTemplate<Object, Object> redisTemplate) {
		return redisTemplate.opsForGeo();
	}

	@Bean
	public HyperLogLogOperations<Object, Object> initHyperLogLog(RedisTemplate<Object, Object> redisTemplate) {
		return redisTemplate.opsForHyperLogLog();
	}

	@Bean
	public ZSetOperations<Object, Object> initZSet(RedisTemplate<Object, Object> redisTemplate) {
		return redisTemplate.opsForZSet();
	}
}