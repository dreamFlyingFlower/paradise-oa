package com.wy.enums;

import com.wy.common.PropConverter;
import com.wy.common.StatusMsg;

/**
 * 用户状态
 *
 * @author ParadiseWY
 * @date 2020年4月8日 上午12:25:18
 */
public enum UserState implements StatusMsg, PropConverter {

	USER_BLACK("黑名单"),
	USER_NORMAL("正常"),
	USER_LOCK("锁定"),
	USER_LEAVE("休假"),
	USER_RESIGNING("离职中"),
	USER_RESIGNED("离职"),
	USER_DELETE("逻辑删除");

	private final String msg;

	UserState(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return this.ordinal();
	}

	public String getMsg() {
		return this.msg;
	}

	@Override
	public Object getValue() {
		return msg;
	}
}