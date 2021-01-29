package com.wy.common;

/**
 * @apiNote 不可修改公共配置类
 * @author ParadiseWY
 * @date 2020年3月17日 上午9:16:22
 */
public interface Constants {

	/** 超级管理员角色编码 */
	String SUPER_ADMIN = "SUPER_ADMIN";

	/** 密码存入数据库的MD5加密的密钥 */
	String SECRET_KEY_DB = "#$%^&*()$%^#$%^&";

	/** 默认分页时每页显示数据量 */
	int PAGE_SIZE = 10;

	/** 验证码redis key */
	String REDIS_KEY_CAPTCHA_CODE = "key_captcha_code:";

	/** 登录用户信息redis key */
	String REDIS_KEY_USER_LOGIN = "key_user_login:";

	/** 登录用户进行jwt加密的token key */
	String JWT_TOKEN_KEY = "login_token";

	/** http请求头中携带token的字段 */
	String HTTP_TOKEN_AUTHENTICATION = "Authorization";

	/** http请求中token值的令牌前缀 */
	String HTTP_TOKEN_PREFIX = "Bearer ";

	/** 验证码有效期,单位分钟 */
	int CAPTCHA_EXPIRATION = 2;

	// /**
	// * 用户名称
	// */
	// public static final String JWT_USERNAME = Claims.SUBJECT;
	//
	// /**
	// * 用户权限
	// */
	// public static final String JWT_AUTHORITIES = "authorities";
	//
}