package com.wy.properties;

import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

/**
 * token相关配置
 * 
 * @author 飞花梦影
 * @date 2021-01-19 22:54:09
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class TokenProperties {

	/** 登录token的超时时间,默认30分钟 */
	private long expireTime = 30;

	/** 登录token的超时时间单位,默认分钟 */
	private TimeUnit expireUnit = TimeUnit.MINUTES;

	/** 重置token过期时间,不能超过expireTime,默认超过20分钟重置 */
	private long flushTime = 20;

	/** 请求头中携带token的字段 */
	private String authentication = "Authorization";

	/** token密钥 */
	private String secret = "!@#$%^&*()QWERTY";
}