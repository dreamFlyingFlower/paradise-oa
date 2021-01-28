package com.wy.model.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限对象
 * 
 * @author 飞花梦影
 * @date 2021-01-20 23:22:51
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel("权限对象")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PermissionVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单编号
	 */
	@ApiModelProperty("菜单编号")
	private Long menuId;

	/**
	 * 菜单名称
	 */
	@ApiModelProperty("菜单名称")
	private String menuName;

	/**
	 * 上级菜单编号
	 */
	@ApiModelProperty("上级菜单编号")
	private Long pid;

	/**
	 * 角色编号
	 */
	@ApiModelProperty("角色编号")
	private Long roleId;

	/**
	 * 角色编码
	 */
	@ApiModelProperty("角色编码")
	private String roleCode;

	/**
	 * 权限字符串,多个用逗号隔开
	 */
	@ApiModelProperty("权限字符串,多个用逗号隔开")
	private String permissions;

	/**
	 * 子菜单权限
	 */
	@ApiModelProperty("子菜单权限")
	private List<PermissionVo> children;
}