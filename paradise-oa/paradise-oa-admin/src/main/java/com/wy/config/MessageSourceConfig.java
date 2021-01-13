package com.wy.config;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 自定义国际化,会自动加载资源目录下的i18n.message目录中的properties文件
 * 
 * @author ParadiseWY
 * @date 2020年4月4日 下午6:44:08
 */
@Configuration
@ConditionalOnMissingBean(MessageSource.class)
public class MessageSourceConfig {

	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource bundleMessageSource = new ReloadableResourceBundleMessageSource();
		bundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
		return bundleMessageSource;
	}
}