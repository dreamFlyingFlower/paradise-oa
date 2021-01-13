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

	PERMISSION_ALL(1, "所有数据权限"),
	PERMISSION_CUSTOM(2, "自定义数据权限"),
	PERMISSION_DEPART(3, "本部门数据权限"),
	PERMISSION_DEPARTS(4, "本部门及以下数据权限");

	private int code;

	private String desc;

	private Permission() {
	}

	private Permission(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return this.code;
	}

	@Override
	public Object getValue() {
		return this.desc;
	}
}