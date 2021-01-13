package com.wy.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.wy.base.AbstractModel;
import com.wy.database.annotation.Pid;
import com.wy.database.annotation.Pri;
import com.wy.database.annotation.Sort;
import com.wy.valid.ValidEdit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 菜单表 ts_menu
 * 
 * @author 飞花梦影
 * @date 2021-01-13 16:37:05
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "菜单表 ts_menu")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单编号
	 */
	@ApiModelProperty("菜单编号")
	@Pri
	@NotNull(groups = ValidEdit.class)
	private Long menuId;

	/**
	 * 菜单名称
	 */
	@ApiModelProperty("菜单名称")
	@NotBlank
	private String menuName;

	/**
	 * 上级菜单编号
	 */
	@ApiModelProperty("上级菜单编号")
	@NotNull
	@Pid
	private Long pid;

	/**
	 * 菜单跳转url,可能带参数
	 */
	@ApiModelProperty("菜单跳转url,可能带参数")
	private String menuPath;

	/**
	 * 菜单视图地址
	 */
	@ApiModelProperty("菜单视图地址")
	private String menuView;

	/**
	 * vue前端路由所需name属性
	 */
	@ApiModelProperty("vue前端路由所需name属性")
	private String routerName;

	/**
	 * 菜单图标,必填,默认star.svg
	 */
	@ApiModelProperty("菜单图标,必填,默认star.svg")
	private String menuIcon;

	/**
	 * 菜单国际化字段,可做唯一标识
	 */
	@ApiModelProperty("菜单国际化字段,可做唯一标识")
	private String menuI18n;

	/**
	 * 带下级页面的上级页面重定向页面
	 */
	@ApiModelProperty("带下级页面的上级页面重定向页面")
	private String redirect;

	/**
	 * 是否隐藏:默认0不隐藏;1隐藏
	 */
	@ApiModelProperty("是否隐藏:默认0不隐藏;1隐藏")
	private Integer hidden;

	/**
	 * 是否外链:默认0否;1是
	 */
	@ApiModelProperty("是否外链:默认0否;1是")
	private Integer link;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	@Sort
	private Integer sortIndex;

	/** 非数据库字段 */
}