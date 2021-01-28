package com.wy.component;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * 国际化配置使用
 * 
 * @author 飞花梦影
 * @date 2021-01-28 17:28:20
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class MessageService {

	@Autowired
	private MessageSource messageSource;
	
	public String getMessage(String code) {
		return messageSource.getMessage(code, null, Locale.getDefault());
	}
}