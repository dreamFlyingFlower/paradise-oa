package com.wy.properties;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录配置
 * 
 * @author 飞花梦影
 * @date 2021-01-20 14:38:10
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class LoginProperties {

	/** 是否使用登录锁定功能,默认false不使用 */
	private boolean validLock = false;

	/** 连续登录次数限制,超过该次数锁定用户 */
	private int count = 5;

	/** 多长时间内连续登录错误指定次数,进行锁定,单位分钟 */
	private int duration = 10;

	/** 被锁定之后需要等待多久才可以重新进行登录操作,单位分钟 */
	private int interval = 5;

	/** 登录可用帐号类型,与数据库字段对应,逗号隔开 */
	private List<String> ACCOUNT_TYPE = Arrays.asList("username", "mobile", "email");

	/** 密码从前端传到后台的解密密钥,应和前端的key一样 */
	private String secretKeyUser = "@#$%^&@#$%^#$%^!";
}