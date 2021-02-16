package com.wy.properties;

import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

/**
 * 验证码参数
 * 
 * @author 飞花梦影
 * @date 2021-01-20 13:06:44
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class CaptchaProperties {

	/** 是否检查验证码 */
	private boolean valid = false;

	/** 验证码图形宽度,默认120px */
	private int width = 120;

	/** 验证码图形高度,默认35px */
	private int height = 35;

	/** 验证码长度,默认4 */
	private int length = 4;

	/** 验证码过期时间,默认1分钟 */
	private TimeUnit expireUnit = TimeUnit.MINUTES;

	/** 验证码过期时间,默认1分钟 */
	private int expireTime = 1;
}