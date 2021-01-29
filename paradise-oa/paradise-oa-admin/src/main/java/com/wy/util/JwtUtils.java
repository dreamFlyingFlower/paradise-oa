package com.wy.util;

import java.util.Date;
import java.util.Map;

import com.wy.common.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT工具类
 * 
 * @author 飞花梦影
 * @date 2021-01-20 09:52:06
 * @git {@link https://github.com/mygodness100}
 */
public class JwtUtils {

	/**
	 * jwt对token进行加密生成jwt令牌
	 * 
	 * @param claims 数据声明
	 * @param secret 秘钥
	 * @return 令牌
	 */
	public static String buildToken(Map<String, Object> claims, String secret) {
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * jwt对token进行加密生成jwt令牌
	 * 
	 * @param claims 数据声明
	 * @param secret 秘钥
	 * @param expireTime 过期时间,单位毫秒
	 * @return 令牌
	 */
	public static String buildToken(Map<String, Object> claims, String secret, long expireTime) {
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret)
				.setExpiration(new Date(System.currentTimeMillis() + expireTime < 0 ? 30 * 60 * 1000 : expireTime))
				.compact();
	}

	/**
	 * 从令牌中获取数据声明
	 * 
	 * @param token 令牌
	 * @param secret 秘钥
	 * @return 数据声明,一个map
	 */
	public static Claims parseTokenBody(String token, String secret) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * 从令牌中获取数据声明的token值
	 * 
	 * @param token 令牌
	 * @param secret 秘钥
	 * @return 数据声明
	 */
	public static String parseToken(String token, String secret) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return (String) claims.get(Constants.JWT_TOKEN_KEY);
	}

	/**
	 * 检查令牌是否过期
	 * 
	 * @param token token值
	 * @param secret 秘钥
	 * @return true->过期,false未过期
	 */
	public static boolean isExpiration(String token, String secret) {
		return parseTokenBody(token, secret).getExpiration().before(new Date());
	}
}