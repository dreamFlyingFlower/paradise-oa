package com.wy.model;

import com.wy.base.AbstractModel;
import com.wy.database.annotation.Pri;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 菜单按钮表 ts_button
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "菜单按钮表 ts_button")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Button extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 按钮编号
	 */
	@ApiModelProperty("按钮编号")
	@Pri
	private Long buttonId;

	/**
	 * 按钮名称
	 */
	@ApiModelProperty("按钮名称")
	private String buttonName;

	/**
	 * 所属菜单编号
	 */
	@ApiModelProperty("所属菜单编号")
	private Long menuId;

	/** 非数据库字段 */
}