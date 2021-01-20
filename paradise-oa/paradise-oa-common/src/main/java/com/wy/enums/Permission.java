package com.wy.enums;

import com.wy.common.PropConverter;

/**
 * 权限枚举
 * 
 * @author ParadiseWY
 * @date 2020-11-25 00:00:11
 * @git {@link https://github.com/mygodness100}
 */
public enum Permission implements PropConverter {

	/** 所有权限 */
	ALL("ALL"),
	INSERT("INSERT"),
	DELETE("DELETE"),
	UPDATE("UPDATE"),
	SELECT("SELECT"),
	IMPORT("IMPORT"),
	EXPORT("EXPORT");

	private String msg;

	private Permission(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return this.ordinal();
	}

	@Override
	public Object getValue() {
		return this.msg;
	}
}