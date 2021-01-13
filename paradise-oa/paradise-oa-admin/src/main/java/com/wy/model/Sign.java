package com.wy.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.wy.base.AbstractModel;
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
 * 签到表 tb_sign
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "签到表 tb_sign")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Sign extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 签到编号
	 */
	@ApiModelProperty("签到编号")
	@NotNull(groups = ValidEdit.class)
	private Long signId;

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	@NotNull
	private Long userId;

	/**
	 * 签到开始时间
	 */
	@ApiModelProperty("签到开始时间")
	@NotNull
	private Date starttime;

	/**
	 * 签到结束时间
	 */
	@ApiModelProperty("签到结束时间")
	@NotNull
	private Date finishtime;

	/**
	 * 是否请假:默认0不是;1是
	 */
	@ApiModelProperty("是否请假:默认0不是;1是")
	@NotNull
	private Integer leave;

	/**
	 * 是否迟到:默认0不是;1是
	 */
	@ApiModelProperty("是否迟到:默认0不是;1是")
	@NotNull
	private Integer late;

	/**
	 * 是否早退:默认0不是;1是
	 */
	@ApiModelProperty("是否早退:默认0不是;1是")
	@NotNull
	private Integer early;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;

	/** 非数据库字段 */
}