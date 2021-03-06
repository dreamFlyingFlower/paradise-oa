package com.wy.util;

import com.wy.utils.StrUtils;

/**
 * sql操作工具类
 *
 * @author ParadiseWY
 * @date 2020年4月8日 上午12:44:22
 */
public class SqlUtil {

	/**
	 * 仅支持字母,数字,下划线,空格,逗号,支持多个字段排序
	 */
	public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,]+";

	/**
	 * 检查字符,防止注入绕过
	 */
	public static String escapeOrderBySql(String value) {
		if (StrUtils.isNotBlank(value) && !isValidOrderBySql(value)) {
			return null;
		}
		return value;
	}

	/**
	 * 验证order by语法是否符合规范
	 */
	public static boolean isValidOrderBySql(String value) {
		return value.matches(SQL_PATTERN);
	}
}