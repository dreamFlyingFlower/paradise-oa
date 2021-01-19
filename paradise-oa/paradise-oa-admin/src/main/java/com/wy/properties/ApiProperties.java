package com.wy.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录时的校验
 * 
 * @author 飞花梦影
 * @date 2021-01-13 10:12:46
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class ApiProperties {

	/** 是否进行api校验,默认不校验 */
	private boolean validApi = false;

	/** 是否检查验证码 */
	private boolean validCode = false;

	/** 是否验证短信验证码 */
	private boolean validSms = false;

	/** 不需要进行校验的api列表 */
	private List<String> excludeApis = new ArrayList<>(Arrays.asList("/login", "/user/login"));

	/** 需要进行校验的api列表,默认null表示全部都要拦截 */
	private List<String> includeApis;

	/** 登录token的超时时间,默认30分钟 */
	private long tokenExpireTime = 30;

	/** 登录token的超时时间单位,默认分钟 */
	private TimeUnit tokenExpireUnit = TimeUnit.MINUTES;

	/** 重置token过期时间检查间隔,默认60秒,单位秒 */
	private long tokenFlushTime = 60;

	/** 请求头中携带token的字段 */
	private String tokenAuthentication = "Authorization";

	/** token密钥 */
	private String tokenSecret = "!@#$%^&*()QWERTY";
}