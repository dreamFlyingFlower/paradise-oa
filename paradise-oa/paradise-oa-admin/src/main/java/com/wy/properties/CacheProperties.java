package com.wy.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 缓存配置
 * 
 * @author 飞花梦影
 * @date 2021-01-13 10:13:05
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class CacheProperties {

	/** redis缓存中验证码key缓存前缀 */
	private String identifyCodeKey = "identify_code:";

	/** redis缓存中验证码过期时间,单位120s */
	private int identifyCodeExpireTime = 120;
}