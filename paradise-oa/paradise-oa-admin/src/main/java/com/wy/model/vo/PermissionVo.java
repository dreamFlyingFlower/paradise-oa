package com.wy.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限实体类,只在security校验中使用
 * 
 * @author 飞花梦影
 * @date 2021-02-15 16:48:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PermissionVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色编号
	 */
	@ApiModelProperty("角色编号")
	private Long roleId;

	/**
	 * 角色类型
	 */
	@ApiModelProperty("角色类型")
	private Integer roleType;

	/**
	 * 角色编码
	 */
	@ApiModelProperty("角色编码")
	private String roleCode;

	/**
	 * 菜单编号
	 */
	@ApiModelProperty("菜单编号")
	private Long menuId;

	/**
	 * 权限
	 */
	@ApiModelProperty("权限")
	private String permissions;

	/**
	 * 菜单状态
	 */
	@ApiModelProperty("菜单状态")
	private Integer menuState;
}