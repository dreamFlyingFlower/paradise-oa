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

	/** 不需要进行校验的api列表 */
	private List<String> excludeApis = new ArrayList<>(Arrays.asList("/login", "/user/login"));

	/** 需要进行校验的api列表,默认null表示全部都要拦截 */
	private List<String> includeApis;

	/** 登录token的超时时间,默认30分钟,单位秒 */
	private int tokenExpireTime = 1800;

	/** token默认过期时间的单位,秒 */
	private TimeUnit tokenTimeUnit = TimeUnit.SECONDS;

	/** 重置token过期时间检查间隔,默认60秒,单位秒 */
	private long tokenFlushTime = 60;

	/** 请求头中携带token的字段 */
	private String tokenAuthentication = "Authorization";

	/** token密钥 */
	private String tokenSecret = "!@#$%^&*()QWERTY";
}