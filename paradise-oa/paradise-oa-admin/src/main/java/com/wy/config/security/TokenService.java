package com.wy.config.security;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.wy.common.Constants;
import com.wy.crypto.CryptoUtils;
import com.wy.model.User;
import com.wy.properties.ConfigProperties;
import com.wy.result.ResultException;
import com.wy.utils.StrUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token验证处理 FIXME
 * 
 * @author 飞花梦影
 * @date 2020-04-01 00:04:49
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class TokenService {

	@Autowired
	private ConfigProperties config;

	protected static final long MILLIS_SECOND = 1000;

	protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

	private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	/** token的组成:32位uuid_用户编号_时间戳 */
	private String tokenFormat = "{0}_{1}_{2}";

	/**
	 * 创建令牌,存储token
	 * 
	 * @param loginUser 用户信息
	 * @return 令牌
	 */
	public void createToken(User user) {
		String token = MessageFormat.format(tokenFormat, CryptoUtils.UUID(), user.getUserId(),
				System.currentTimeMillis());
		refreshToken(user);
		Map<String, Object> claims = new HashMap<>();
		claims.put(config.getCache().getUserLoginKey(), token);
		user.setToken(createToken(claims));
		redisTemplate.opsForValue().set(getTokenKey(user.getUserId()), user);
	}

	/**
	 * 获取用户身份信息
	 * 
	 * @return 用户信息
	 */
	public User getLoginUser(HttpServletRequest request) {
		// 获取请求携带的令牌
		String bearer = getToken(request);
		if (StrUtils.isBlank(bearer)) {
			throw new ResultException("the requset header has bad authentication");
		}
		Claims claims = parseToken(bearer);
		// 解析对应的权限以及用户信息
		String token = (String) claims.get(config.getCache().getUserLoginKey());
		String userKey = getTokenKey(Long.parseLong(token.split("_")[1]));
		return (User) redisTemplate.opsForValue().get(userKey);
	}

	/**
	 * 设置用户身份信息
	 */
	public void setLoginUser(User loginUser) {
		if (Objects.nonNull(loginUser) && StrUtils.isNotBlank(loginUser.getToken())) {
			refreshToken(loginUser);
		}
	}

	/**
	 * 删除用户身份信息
	 */
	public void delLoginUser(Long userId) {
		redisTemplate.delete(getTokenKey(userId));
	}

	/**
	 * 验证令牌有效期,相差不足20分钟,自动刷新缓存
	 * 
	 * @param token 令牌
	 * @return 令牌
	 */
	public void verifyToken(User loginUser) {
		long expireTime = config.getApi().getTokenExpireTime();
		long currentTime = System.currentTimeMillis();
		if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
			refreshToken(loginUser);
		}
	}

	/**
	 * 刷新令牌有效期
	 * 
	 * @param loginUser 登录信息
	 */
	public void refreshToken(User user) {
		redisTemplate.opsForValue().set(getTokenKey(user.getUserId()), user, config.getApi().getTokenExpireTime(),
				config.getApi().getTokenTimeUnit());
	}

	/**
	 * jwt对token进行加密生成jwt令牌
	 * 
	 * @param claims 数据声明
	 * @return 令牌
	 */
	private String createToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, config.getApi().getTokenSecret())
				.compact();
	}

	/**
	 * 从令牌中获取数据声明
	 * 
	 * @param token 令牌
	 * @return 数据声明
	 */
	private Claims parseToken(String token) {
		return Jwts.parser().setSigningKey(config.getApi().getTokenSecret()).parseClaimsJws(token).getBody();
	}

	/**
	 * 获取请求token
	 * 
	 * @param request
	 * @return token
	 */
	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(config.getApi().getTokenAuthentication());
		if (StrUtils.isNotBlank(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
			token = token.replace(Constants.TOKEN_PREFIX, "");
		}
		return token;
	}

	/**
	 * 组装redis中存储的用户token的key
	 * 
	 * @param userId 用户编号
	 * @return key
	 */
	private String getTokenKey(long userId) {
		return config.getCache().getUserTokenKey() + userId;
	}
}