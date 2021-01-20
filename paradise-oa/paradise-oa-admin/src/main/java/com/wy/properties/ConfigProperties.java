package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义配置,放在项目中或者在打包之后放在jar包的同名目录的config文件夹下
 * 
 * @author 飞花梦影
 * @date 2021-01-13 10:13:35
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "config")
public class ConfigProperties {

	/** 通用配置 */
	private CommonProperties common = new CommonProperties();

	/** api配置 */
	private ApiProperties api = new ApiProperties();

	/** redis配置 */
	private CacheProperties cache = new CacheProperties();

	/** 验证码配置 */
	private CaptchaProperties captcha = new CaptchaProperties();

	/** 文件配置 */
	private FileinfoProperties fileinfo = new FileinfoProperties();

	/** 拦截器配置 */
	private FilterProperties filter = new FilterProperties();

	/** 登录配置 */
	private LoginProperties login = new LoginProperties();

	/** rabbitmq配置 */
	private RabbitMqProperties rabbitmq = new RabbitMqProperties();

	/** 短信配置 */
	private SmsProperties sms = new SmsProperties();

	/** token配置 */
	private TokenProperties token = new TokenProperties();
}