package com.wy.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用配置
 * 
 * @author 飞花梦影
 * @date 2021-01-13 10:13:21
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class CommonProperties {

	/** 资源访问前缀 */
	private String resourcesPrefix;

	/** 验证码图形宽度,默认120px */
	private int verifyCodeWidth = 120;

	/** 验证码图形高度,默认35px */
	private int verifyCodeHeight = 35;

	/** 验证码长度,默认4 */
	private int verifyCodeLengh = 4;

	/** 项目名称 */
	private String projectName;

	/** 版本 */
	private String version;

	/** 版权年份 */
	private String copyrightYear;

	/** 上传或下载文件目录,非本系统资源目录 */
	private String profile;

	/** 是否根据ip地址获取地域,默认不获取 */
	private boolean addressOpt = false;

	/** 获取头像上传路径 */
	private String avatarPath = profile + "avatar";

	/** 获取下载路径 */
	private String downloadPath = profile + "download";

	/** 获取上传路径 */
	private String uploadPath = profile + "upload";
}