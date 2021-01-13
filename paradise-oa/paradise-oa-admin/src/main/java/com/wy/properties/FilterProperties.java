package com.wy.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 拦截器参数
 * 
 * @author 飞花梦影
 * @date 2021-01-13 10:14:27
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class FilterProperties {

	/** 是否启动xss拦截,默认启用 */
	private String xssEnabled = "true";

	/** 不被xss拦截器拦截的url */
	private String xssExcludes = "/notice/*";

	/** 被xss拦截器拦截的url */
	private String xssUrlPatterns = "/*";
}