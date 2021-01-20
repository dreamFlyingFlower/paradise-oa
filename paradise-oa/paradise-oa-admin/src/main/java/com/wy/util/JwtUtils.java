package com.wy.util;

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
	 * 从令牌中获取数据声明
	 * 
	 * @param token 令牌
	 * @param secret 秘钥
	 * @return 数据声明
	 */
	public static String parseToken(String token, String secret) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return (String) claims.get(Constants.TOKEN);
	}
}