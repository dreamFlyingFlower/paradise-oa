package com.wy.enums;

import com.wy.common.PropConverter;

/**
 * 操作日志类型
 * 
 * @author 飞花梦影
 * @date 2021-01-13 10:18:54
 * @git {@link https://github.com/mygodness100}
 */
public enum OperatorType implements PropConverter {
	/** 其它 */
	OTHER() {

		@Override
		public Object getValue() {
			return "其他";
		}
	},

	/** 后台用户 */
	MANAGE() {

		@Override
		public Object getValue() {
			return "后台用户";
		}
	},

	/** 手机端用户 */
	MOBILE() {

		@Override
		public Object getValue() {
			return "手机端用户";
		}
	}
}