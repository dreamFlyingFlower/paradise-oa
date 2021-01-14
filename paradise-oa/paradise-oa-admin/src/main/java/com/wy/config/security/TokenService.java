package com.wy.config.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.wy.common.Constants;
import com.wy.crypto.CryptoUtils;
import com.wy.model.User;
import com.wy.properties.ConfigProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @apiNote token验证处理 FIXME
 * @author ParadiseWY
 * @date 2020年4月1日 下午8:33:22
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

	/**
	 * 获取用户身份信息
	 * 
	 * @return 用户信息
	 */
	public User getLoginUser(HttpServletRequest request) {
		// 获取请求携带的令牌
		String token = getToken(request);
		if (StringUtils.isNotEmpty(token)) {
			Claims claims = parseToken(token);
			// 解析对应的权限以及用户信息
			String uuid = (String) claims.get(config.getCache().getUserLoginKey());
			String userKey = getTokenKey(uuid);
			User user = (User) redisTemplate.opsForValue().get(userKey);
			return user;
		}
		return null;
	}

	/**
	 * 设置用户身份信息
	 */
	public void setLoginUser(User loginUser) {
		if (Objects.nonNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
			refreshToken(loginUser);
		}
	}

	/**
	 * 删除用户身份信息
	 */
	public void delLoginUser(String token) {
		if (StringUtils.isNotEmpty(token)) {
			String userKey = getTokenKey(token);
			redisTemplate.delete(userKey);
		}
	}

	/**
	 * 创建令牌
	 * 
	 * @param loginUser 用户信息
	 * @return 令牌
	 */
	public String createToken(User user) {
		String token = CryptoUtils.UUID();
		user.setToken(token);
		refreshToken(user);
		Map<String, Object> claims = new HashMap<>();
		claims.put(config.getCache().getUserLoginKey(), token);
		return createToken(claims);
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
	public void refreshToken(User loginUser) {
		loginUser.setExpireTime(loginUser.getLoginTime()
				+ config.getApi().getTokenTimeUnit().toMillis(config.getApi().getTokenExpireTime()));
		String userKey = getTokenKey(loginUser.getToken());
		redisTemplate.opsForValue().set(userKey, loginUser, config.getApi().getTokenExpireTime(),
				config.getApi().getTokenTimeUnit());
	}

	/**
	 * jwt对token进行加密声场jwt令牌
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
	 * 从令牌中获取用户名
	 * 
	 * @param token 令牌
	 * @return 用户名
	 */
	public String getUsernameFromToken(String token) {
		Claims claims = parseToken(token);
		return claims.getSubject();
	}

	/**
	 * 获取请求token
	 * 
	 * @param request
	 * @return token
	 */
	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(config.getApi().getTokenAuthentication());
		if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
			token = token.replace(Constants.TOKEN_PREFIX, "");
		}
		return token;
	}

	private String getTokenKey(String uuid) {
		return config.getCache().getUserTokenKey() + uuid;
	}
}