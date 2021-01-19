package com.wy.component;

import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.wy.common.Constants;
import com.wy.crypto.CryptoUtils;
import com.wy.model.User;
import com.wy.properties.ConfigProperties;
import com.wy.result.ResultException;
import com.wy.utils.MapUtils;
import com.wy.utils.StrUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token验证处理
 * 
 * @author 飞花梦影
 * @date 2020-04-01 00:04:49
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class TokenService {

	@Autowired
	private ConfigProperties config;

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
		return Constants.REDIS_KEY_TOKEN + userId;
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
		refreshToken(user);
		user.setToken(config.getFilter().isJwtEnable() ? createToken(MapUtils.builder(Constants.TOKEN, token).build())
				: token);
		redisTemplate.opsForValue().set(getTokenKey(user.getUserId()), user);
	}

	/**
	 * jwt对token进行加密生成jwt令牌
	 * 
	 * @param claims 数据声明
	 * @return 令牌
	 */
	private String createToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, config.getToken().getSecret())
				.compact();
	}

	/**
	 * 获取用户身份信息
	 * 
	 * @param request 请求体
	 * @return 用户信息
	 */
	public User getLoginUser(HttpServletRequest request) {
		// 获取请求携带的令牌
		String token = getToken(request);
		if (StrUtils.isBlank(token)) {
			throw new ResultException("the requset header has bad authentication");
		}
		if (config.getFilter().isJwtEnable()) {
			// 解析对应的权限以及用户信息
			token = parseToken(token);
		}
		String userKey = getTokenKey(Long.parseLong(token.split("_")[1]));
		return (User) redisTemplate.opsForValue().get(userKey);
	}

	/**
	 * 获取请求token
	 * 
	 * @param request 请求体
	 * @return token
	 */
	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(config.getToken().getAuthentication());
		if (StrUtils.isNotBlank(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
			token = token.replace(Constants.TOKEN_PREFIX, "");
		}
		return token;
	}

	/**
	 * 从令牌中获取数据声明
	 * 
	 * @param token 令牌
	 * @return 数据声明
	 */
	private String parseToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(config.getToken().getSecret()).parseClaimsJws(token).getBody();
		return (String) claims.get(Constants.TOKEN);
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