package com.wy.common;

/**
 * @apiNote 不可修改公共配置类
 * @author ParadiseWY
 * @date 2020年3月17日 上午9:16:22
 */
public interface Constants {

	/** 密码存入数据库的MD5加密的密钥 */
	String SECRET_KEY_DB = "#$%^&*()$%^#$%^&";

	/** 默认分页时每页显示数据量 */
	int PAGE_SIZE = 10;

	/** 验证码redis key */
	String REDIS_KEY_CAPTCHA_CODE = "key_captcha_code:";

	/** 登录用户redis key */
	String REDIS_KEY_TOKEN = "key_token:";

	/** 用户token */
	String TOKEN = "login_token";

	/** 令牌前缀 */
	String TOKEN_PREFIX = "Bearer ";

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