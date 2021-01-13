package com.wy.enums;

/**
 * 请求来源,web浏览器,安卓(android),ios
 * 
 * @author ParadiseWY
 * @date 2020年4月14日 下午11:21:01
 */
public enum RequestSource {

	/**
	 * web浏览器
	 */
	WEB("W"),
	/**
	 * android系统
	 */
	ANDROID("A"),
	/**
	 * ios系统
	 */
	IOS("I");

	private String source;

	private RequestSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return this.source;
	}
}