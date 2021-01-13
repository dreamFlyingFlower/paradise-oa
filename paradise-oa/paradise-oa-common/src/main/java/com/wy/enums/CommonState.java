package com.wy.enums;

import com.wy.common.PropConverter;

/**
 * 通用状态
 *
 * @author ParadiseWY
 * @date 2020-04-08 12:25:15
 * @git {@link https://github.com/mygodness100}
 */
public enum CommonState implements PropConverter {

	COMMON_STOP() {

		@Override
		public Object getValue() {
			return "异常";
		}
	},

	COMMON_NORMAL() {

		@Override
		public Object getValue() {
			return "正常";
		}
	},

	COMMON_DELETE() {

		@Override
		public Object getValue() {
			return "逻辑删除";
		}
	};
}