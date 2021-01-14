package com.wy.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.wy.base.AbstractModel;
import com.wy.database.annotation.Pri;
import com.wy.database.annotation.Sort;
import com.wy.database.annotation.Unique;
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
 * 部门表 ts_depart
 * 
 * @author 飞花梦影
 * @date 2021-01-13 10:29:22
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "部门表 ts_depart")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Depart extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 部门编号
	 */
	@ApiModelProperty("部门编号")
	@NotNull(groups = ValidEdit.class)
	@Pri
	private Long departId;

	/**
	 * 部门名称
	 */
	@ApiModelProperty("部门名称")
	@NotBlank(message = "部门名称不能为空")
	@Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
	@Unique
	private String departName;

	/**
	 * 上级部门编号
	 */
	@ApiModelProperty("上级部门编号")
	@NotNull
	private Long pid;

	/**
	 * 上级部门名称
	 */
	@ApiModelProperty("上级部门名称")
	private String pname;

	/**
	 * 部门描述
	 */
	@ApiModelProperty("部门描述")
	private String departDesc;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	@Sort
	private Integer sortIndex;

	/** 非数据库字段 */
}