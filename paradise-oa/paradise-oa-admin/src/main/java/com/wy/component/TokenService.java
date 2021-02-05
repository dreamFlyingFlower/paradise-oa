package com.wy.component;

import java.text.MessageFormat;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.wy.common.Constants;
import com.wy.crypto.CryptoUtils;
import com.wy.enums.TipEnum;
import com.wy.exception.AuthException;
import com.wy.model.User;
import com.wy.properties.ConfigProperties;
import com.wy.result.ResultException;
import com.wy.service.impl.UserServiceImpl;
import com.wy.util.JwtUtils;
import com.wy.utils.MapUtils;
import com.wy.utils.StrUtils;

/**
 * token验证处理
 * 
 * @author 飞花梦影
 * @date 2020-04-01 00:04:49
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class TokenService extends MessageService {

	@Autowired
	private ConfigProperties config;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	/** token的组成:32位uuid_用户编号_时间戳 */
	private static final String TOKEN_FORMAT = "{0}_{1}_{2}";

	/**
	 * 组装redis中存储的用户token的key
	 * 
	 * @param userId 用户编号
	 * @return key
	 */
	private String getTokenKey(long userId) {
		return Constants.REDIS_KEY_USER_LOGIN + userId;
	}

	/**
	 * 创建令牌,存储token
	 * 
	 * @param loginUser 用户信息
	 * @return 令牌
	 */
	public void createToken(User user) {
		String token = MessageFormat.format(TOKEN_FORMAT, CryptoUtils.UUID(), user.getUserId(),
				System.currentTimeMillis());
		user.setToken(
				config.getFilter().isJwtEnable()
						? JwtUtils.buildToken(MapUtils.builder(Constants.JWT_TOKEN_KEY, token).build(),
								config.getToken().getSecret())
						: token);
		redisTemplate.opsForValue().set(getTokenKey(user.getUserId()), user, config.getToken().getExpireTime(),
				config.getToken().getExpireUnit());
	}

	/**
	 * 直接从redis中获取用户身份信息
	 * 
	 * @param userId 用户编号
	 * @return 用户信息
	 */
	public User getLoginUser(Long userId) {
		return (User) redisTemplate.opsForValue().get(getTokenKey(userId));
	}

	/**
	 * 直接从redis中获取用户身份信息
	 * 
	 * @param userId 用户编号
	 * @return 用户信息
	 */
	public User getLoginUser(String username) {
		User user = userService.getByUsername(username);
		if (Objects.isNull(user)) {
			throw new ResultException(TipEnum.TIP_USER_NOT_EXIST);
		}
		return (User) redisTemplate.opsForValue().get(getTokenKey(user.getUserId()));
	}

	/**
	 * 从请求中获取用户身份信息
	 * 
	 * @param request 请求体
	 * @return 用户信息
	 */
	public User getLoginUser(HttpServletRequest request) {
		// 获取请求携带的令牌
		String token = getToken(request);
		if (StrUtils.isBlank(token)) {
			throw new AuthException(TipEnum.TIP_AUTH_TOKEN_EMPTY);
		}
		if (config.getFilter().isJwtEnable()) {
			// 解析对应的权限以及用户信息
			token = JwtUtils.parseToken(token, config.getToken().getSecret());
		}
		try {
			String userKey = getTokenKey(Long.parseLong(token.split("_")[1]));
			Object object = redisTemplate.opsForValue().get(userKey);
			return JSON.parseObject(JSON.toJSONString(object), User.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResultException(TipEnum.TIP_AUTH_TOKEN_ILLEGAL);
		}
	}

	/**
	 * 获取请求token
	 * 
	 * @param request 请求体
	 * @return token
	 */
	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(Constants.HTTP_TOKEN_AUTHENTICATION);
		if (StrUtils.isNotBlank(token) && token.startsWith(Constants.HTTP_TOKEN_PREFIX)) {
			token = token.replace(Constants.HTTP_TOKEN_PREFIX, "");
		}
		return token;
	}

	/**
	 * 删除用户身份信息
	 * 
	 * @param userId 用户编号
	 */
	public void delLoginUser(Long userId) {
		redisTemplate.delete(getTokenKey(userId));
	}

	/**
	 * 验证令牌有效期,超过20分钟,自动刷新缓存
	 * 
	 * @param token 令牌
	 * @return 令牌
	 */
	public void verifyToken(User user) {
		Long expireTime = redisTemplate.getExpire(getTokenKey(user.getUserId()));
		if (config.getToken().getExpireUnit().toSeconds(config.getToken().getExpireTime())
				- config.getToken().getExpireUnit().toSeconds(config.getToken().getFlushTime()) > expireTime) {
			refreshToken(user);
		}
	}

	/**
	 * 刷新缓存中的用户信息
	 * 
	 * @param user 用户信息
	 */
	public void refreshToken(User user) {
		redisTemplate.opsForValue().set(getTokenKey(user.getUserId()), user, config.getToken().getExpireTime(),
				config.getToken().getExpireUnit());
	}
}