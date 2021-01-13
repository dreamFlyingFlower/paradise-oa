package com.wy.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.wy.utils.StrUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @description 可配置文件属性
 * @author paradiseWy 2018年10月12日
 */
@Slf4j
public class Props {

	private static Properties props;

	/**
	 * 非配置文件属性,判断是否已经新建了当天的文件存放文件夹
	 */
	public static boolean isTodayFolder = false;

	/**
	 * 登录可用帐号类型,与数据库字段对应,逗号隔开
	 */
	public static final String ACCOUNT_TYPE;

	/**
	 * 默认菜单图标名称
	 */
	public static final String DEFAULT_USER_ICON;

	/**
	 * 默认用户图标
	 */
	public static final String DEFAULT_MENU_ICON;

	/**
	 * 上传文件本地存储地址
	 */
	public static final String FILE_LOCAL;

	/**
	 * 服务器访问本地文件地址;不在数据库中存储http地址,防止http变动文件访问不了
	 */
	public static final String FILE_HTTP;

	/**
	 * 密码从前端传到后台的解密密钥,应和前端的key一样
	 */
	public static final String SECRET_KEY_USER;

	/**
	 * 密码未设置时的默认密码
	 */
	public static final String DEFAULT_PWD;

	static {
		props = new Properties();
		try (InputStream is = Props.class.getClassLoader().getResourceAsStream("props.properties")) {
			props.load(is);
		} catch (IOException e) {
			log.error("可配置文件props.properties初始化失败");
			e.printStackTrace();
		}
		ACCOUNT_TYPE = props.getProperty("account_type", "username");
		DEFAULT_USER_ICON = props.getProperty("default_user_icon");
		DEFAULT_MENU_ICON = props.getProperty("default_menu_icon");
		FILE_LOCAL = props.getProperty("file_local");
		FILE_HTTP = props.getProperty("file_http");
		SECRET_KEY_USER = props.getProperty("secret_key_user");
		DEFAULT_PWD = props.getProperty("default_pwd");
	}

	public static Properties getProps() {
		return props;
	}

	public static String getStr(String key) {
		return props.getProperty(key);
	}

	public static String getStr(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	public static int getInt(String key) {
		return Integer.parseInt(StrUtils.isBlank(props.getProperty(key)) ? "-1" : props.getProperty(key));
	}

	public static int getInt(String key, int defaultValue) {
		return StrUtils.isBlank(props.getProperty(key)) ? defaultValue : Integer.parseInt(props.getProperty(key));
	}

	public static long getLong(String key) {
		return Long.parseLong(StrUtils.isBlank(props.getProperty(key)) ? "-1" : props.getProperty(key));
	}

	public static long getLong(String key, long defaultValue) {
		return StrUtils.isBlank(props.getProperty(key)) ? defaultValue : Long.parseLong(props.getProperty(key));
	}
}