package com.wy.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 短信配置
 * 
 * @author 飞花梦影
 * @date 2021-01-20 13:09:03
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class SmsProperties {

	/** 是否验证短信验证码 */
	private boolean valid = false;
}