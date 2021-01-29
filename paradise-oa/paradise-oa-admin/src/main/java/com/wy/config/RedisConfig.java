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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis自定义配置,使用jackson2序列化
 * 
 * @author ParadiseWY
 * @date 2020-04-02 16:26:04
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

	@Bean
	@ConditionalOnMissingBean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		// 使用fastjson序列化
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		template.setDefaultSerializer(redisSerializer);
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