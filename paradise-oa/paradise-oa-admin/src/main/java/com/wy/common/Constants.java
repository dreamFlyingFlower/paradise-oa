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

	/** 原始密码最小长度 */
	int PWD_MIN_LENGTH = 6;

	/** 原始密码最大长度 */
	int PWD_MAX_LENGTH = 16;

	/** 默认分页时每页显示数据量 */
	int PAGE_SIZE = 10;

	/** 验证码redis key前缀 */
	String REDIS_KEY_CAPTCHA_CODE = "key_captcha_code:";

	/** 登录用户信息redis key前缀 */
	String REDIS_KEY_USER_LOGIN = "key_user_login:";

	/** 防止重复提交提前存入的redis key前缀 */
	String REDIS_KEY_IDEMPOTENCY = "key_idempotency:";

	/** 登录用户进行jwt加密的token key */
	String JWT_TOKEN_KEY = "login_token";

	/** http请求头中携带token key */
	String HTTP_TOKEN_AUTHENTICATION = "Authorization";

	/** http请求头中携带token value令牌前缀 */
	String HTTP_TOKEN_PREFIX = "Bearer ";

	/** http请求头中携带重复请求的token key */
	String HTTP_TOKEN_IDEMPOTENCY = "TokenIdempotency";

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