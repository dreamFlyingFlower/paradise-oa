package com.wy.enums;

import com.wy.common.PropConverter;

/**
 * 业务操作类型
 * 
 * @author 飞花梦影
 * @date 2021-01-13 10:18:06
 * @git {@link https://github.com/mygodness100}
 */
public enum BusinessType implements PropConverter {

	/** 其它 */
	OTHER("其他"),

	/** 新增 */
	INSERT("新增"),

	/** 修改 */
	UPDATE("修改"),

	/** 删除 */
	DELETE("删除"),

	/** 授权 */
	GRANT("授权"),

	/** 导出 */
	EXPORT("导出"),

	/** 导入 */
	IMPORT("导入"),

	/** 清空数据 */
	CLEAN("清空数据");

	private String desc;

	private BusinessType() {}

	private BusinessType(String desc) {
		this.desc = desc;
	}

	@Override
	public Object getValue() {
		return this.desc;
	}
}