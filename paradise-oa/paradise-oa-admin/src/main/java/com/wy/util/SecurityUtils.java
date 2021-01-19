package com.wy.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.wy.config.security.LoginAuthenticationProvider;
import com.wy.enums.TipEnum;
import com.wy.exception.AuthException;
import com.wy.model.User;

/**
 * 安全服务工具类
 * 
 * @author 飞花梦影
 * @date 2021-01-14 15:02:28
 * @git {@link https://github.com/mygodness100}
 */
public class SecurityUtils {

	/**
	 * 获取Authentication认证信息
	 */
	public static Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			throw new AuthException(TipEnum.TIP_LOGIN_FAIL_NOT_LOGIN);
		}
		return authentication;
	}

	/**
	 * 获取用户,需要和登录时存入缓存的对象相同 {@link LoginAuthenticationProvider#authenticate}
	 */
	public static User getLoginUser() {
		return (User) getAuthentication().getPrincipal();
	}

	/**
	 * 生成BCryptPasswordEncoder密码,每次加密都不同,加密后的长度为60,且被加密的字符串不得超过72
	 * 
	 * @param password 密码
	 * @return 加密字符串
	 */
	public static String encode(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}

	/**
	 * 判断密码是否相同
	 * 
	 * @param originlPwd 真实密码,未加密
	 * @param encodedPwd 加密后字符
	 * @return 结果
	 */
	public static boolean matches(String originlPwd, String encodedPwd) {
		return new BCryptPasswordEncoder().matches(originlPwd, encodedPwd);
	}
}