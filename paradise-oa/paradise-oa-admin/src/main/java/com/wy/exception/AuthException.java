package com.wy.exception;

import org.springframework.security.core.AuthenticationException;

import com.wy.enums.TipEnum;

/**
 * @apiNote 自定义安全认证请求异常
 * @apiNote 在securiy的所有请求中,必须继承AuthenticationException,否则自定义异常无法正确返回给前端
 * @author ParadiseWY
 * @date 2020年4月2日 下午11:23:56
 */
public class AuthException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	private int code;

	public int getCode() {
		return code;
	}

	public AuthException() {
		this(TipEnum.TIP_SYS_ERROR);
	}

	public AuthException(TipEnum tipEnum) {
		this(tipEnum.getCode(), tipEnum.getMsg(), null);
	}

	public AuthException(CharSequence message) {
		this(0, message);
	}

	public AuthException(int code, CharSequence message) {
		this(code, message, null);
	}

	public AuthException(int code, Throwable ex) {
		this(code, ex.getMessage(), ex);
	}

	public AuthException(CharSequence message, Throwable ex) {
		this(0, message, ex);
	}

	public AuthException(int code, CharSequence message, Throwable ex) {
		super(message.toString(), ex);
		this.code = code;
	}
}