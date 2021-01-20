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
	PERMISSION_ALL("ALL"),
	PERMISSION_INSERT("INSERT"),
	PERMISSION_DELETE("DELETE"),
	PERMISSION_UPDATE("UPDATE"),
	PERMISSION_SELECT("SELECT"),
	PERMISSION_IMPORT("IMPORT"),
	PERMISSION_EXPORT("EXPORT");

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