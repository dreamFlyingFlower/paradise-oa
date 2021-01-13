package com.wy.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
 * 公告表 ts_notice
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "公告表 ts_notice")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 公告编号
	 */
	@ApiModelProperty("公告编号")
	@Pri
	private Long noticeId;

	/**
	 * 公告标题
	 */
	@ApiModelProperty("公告标题")
	@NotBlank(message = "公告标题不能为空")
	@Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
	private String noticeTitle;

	/**
	 * 公告类型:默认1通知,见ts_dict表NOTICE_TYPE
	 */
	@ApiModelProperty("公告类型:默认1通知,见ts_dict表NOTICE_TYPE")
	private Integer noticeType;

	/**
	 * 公告内容,不超过2000字
	 */
	@ApiModelProperty("公告内容,不超过2000字")
	private String noticeContent;

	/**
	 * 公告状态:0关闭;默认1正常
	 */
	@ApiModelProperty("公告状态:0关闭;默认1正常")
	private Integer state;

	/**
	 * 公告时间
	 */
	@ApiModelProperty("公告时间")
	private Date createtime;

	/** 非数据库字段 */
}