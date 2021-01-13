package com.wy.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @apiNote 前端vue路由显示信息
 * @author ParadiseWY
 * @date 2020年4月1日 下午5:15:24
 */
@ApiModel("前端vue路由显示信息")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MetaVo {

	/**
	 * 设置该路由在侧边栏和面包屑中展示的名字
	 */
	@ApiModelProperty("设置该路由在侧边栏和面包屑中展示的名字")
	private String title;

	/**
	 * 设置该路由的图标,对应路径src/icons/svg
	 */
	@ApiModelProperty("设置该路由的图标,对应路径src/icons/svg")
	private String icon;
}