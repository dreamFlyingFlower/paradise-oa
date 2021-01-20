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

	/** 项目名称 */
	private String projectName;

	/** 版本 */
	private String version;

	/** 版权年份 */
	private String copyrightYear;

	/** 是否根据ip地址获取地域,默认不获取 */
	private boolean addressOpt = false;

	/** 密码未设置时的默认密码 */
	private String defaultPwd = "66668888";

	/** 默认菜单图标名称 */
	private String defaultMenuIcon = "star.svg";

	/** 默认用户图标 */
	private String defaultUserIcon = "user.png";
}