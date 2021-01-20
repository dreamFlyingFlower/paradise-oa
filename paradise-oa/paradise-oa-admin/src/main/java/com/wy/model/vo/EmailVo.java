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
 * 邮件实体类
 * 
 * @author 飞花梦影
 * @date 2021-01-18 11:09:48
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel("邮件实体类")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 接收者邮件地址
	 */
	@ApiModelProperty("接收者邮件地址")
	private List<String> toAddress;

	/**
	 * 邮件主题
	 */
	@ApiModelProperty("邮件主题")
	private String subject;

	/**
	 * 邮件内容
	 */
	@ApiModelProperty("邮件内容")
	private String content;

	/**
	 * 抄送地址
	 */
	@ApiModelProperty("抄送对象")
	private List<String> cc;

	/**
	 * 密送地址
	 */
	@ApiModelProperty("密送地址")
	private List<String> bcc;

	/**
	 * 附件
	 */
	@ApiModelProperty("附件")
	private List<String> attachs;

	/**
	 * 内嵌内容
	 */
	@ApiModelProperty("内嵌内容")
	private List<String> relateds;
}