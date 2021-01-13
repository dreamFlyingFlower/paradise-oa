package com.wy.enums;

import com.wy.common.PropConverter;

/**
 * 用户状态
 *
 * @author ParadiseWY
 * @date 2020年4月8日 上午12:25:18
 */
public enum UserState implements PropConverter {

	USER_BLACK(0, "黑名单"),
	USER_NORMAL(1, "正常"),
	USER_LEAVE(2, "放假中"),
	USER_RESIGNING(3, "离职中"),
	USER_DELETE(4, "逻辑删除");

	private final int code;

	private final String info;

	UserState(int code, String info) {
		this.code = code;
		this.info = info;
	}

	public int getCode() {
		return code;
	}

	public String getInfo() {
		return info;
	}

	@Override
	public Object getValue() {
		return info;
	}
}