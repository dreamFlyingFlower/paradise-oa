package com.wy.model;

import com.wy.base.AbstractModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色部门关联表 tr_role_depart
 * 
 * @author 飞花梦影
 * @date 2021-01-13 11:01:35
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "角色部门关联表 tr_role_depart")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleDepart extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色编号
	 */
	@ApiModelProperty("角色编号")
	private Long roleId;

	/**
	 * 部门编号
	 */
	@ApiModelProperty("部门编号")
	private Long departId;

	/** 非数据库字段 */
}