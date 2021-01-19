package com.wy.model.vo;

import java.util.Date;

import com.wy.enums.FileType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 上传文件的基本信息
 * 
 * @author 飞花梦影
 * @date 2021-01-19 14:20:31
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel("邮件实体类")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileinfoVo {

	/**
	 * 文件的原始名称
	 */
	@ApiModelProperty("文件的原始名称")
	private String originalName;

	/**
	 * 文件存在本地的名称
	 */
	@ApiModelProperty("文件存在本地的名称")
	private String localName;

	/**
	 * 文件类型
	 */
	@ApiModelProperty("文件类型")
	private FileType fileType;

	/**
	 * 文件大小,单位M
	 */
	@ApiModelProperty("文件大小,单位M")
	private double fileSize;

	/**
	 * 音视频文件时长
	 */
	@ApiModelProperty("音视频文件时长,可能为null")
	private String fileTime;

	/**
	 * 文件后缀
	 */
	@ApiModelProperty("文件后缀,不带.,可能为null")
	private String fileSuffix;
	
	/**
	 * 文件上传时间
	 */
	@ApiModelProperty("文件上传时间")
	private Date createtime;
}