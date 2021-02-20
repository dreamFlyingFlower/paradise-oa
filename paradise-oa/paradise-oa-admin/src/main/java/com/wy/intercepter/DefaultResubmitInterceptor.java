package com.wy.intercepter;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.wy.common.Constants;
import com.wy.component.RedisService;
import com.wy.enums.TipEnum;
import com.wy.result.ResultException;
import com.wy.utils.StrUtils;

/**
 * 判断是否重复请求
 * 
 * 需要判断重复提交的接口应该在提交之前先请求后台获得一个随机token,提交时该token需要传入到header中返回;
 * 后台从请求头中获得指定参数,再从redis中查找是否存在该值,存在则是第一次请求,删除缓存;若不存在则是重复请求
 *
 * @author 飞花梦影
 * @date 2021-02-02 16:51:54
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
public class DefaultResubmitInterceptor extends IdempotencyInterceptor {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	private RedisService redisService;

	@Override
	public boolean isResubmit(HttpServletRequest request) {
		String tokenIdempotency = request.getHeader(Constants.HTTP_TOKEN_IDEMPOTENCY);
		if (StrUtils.isBlank(tokenIdempotency)) {
			throw new ResultException(TipEnum.TIP_PARAM);
		}
		// 从redis中取值,若存在说明是第一次提交,删除redis中的值;若是重复提交,则不存在
		Object value = redisTemplate.opsForValue().get(Constants.REDIS_KEY_IDEMPOTENCY + tokenIdempotency);
		if (Objects.isNull(value)) {
			Long row = redisService.deleteSafe(Constants.REDIS_KEY_IDEMPOTENCY + tokenIdempotency, value);
			if (row != 0L) {
				return true;
			}
		}
		return false;
	}
}