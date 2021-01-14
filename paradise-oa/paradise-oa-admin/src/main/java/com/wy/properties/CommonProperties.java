package com.wy.properties;

import java.util.Arrays;
import java.util.List;

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

	/** 是否根据ip地址获取地域,默认不获取 */
	private boolean addressOpt = false;

	/** 登录可用帐号类型,与数据库字段对应,逗号隔开 */
	private List<String> ACCOUNT_TYPE = Arrays.asList("username", "mobile", "email");

	/** 密码未设置时的默认密码 */
	private String defaultPwd = "66668888";

	/** 密码从前端传到后台的解密密钥,应和前端的key一样 */
	private String secretKeyUser = "@#$%^&@#$%^#$%^!";

	/** 默认菜单图标名称 */
	private String defaultMenuIcon = "star.svg";

	/** 默认用户图标 */
	private String defaultUserIcon = "user.png";
}