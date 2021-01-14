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

	/** api相关配置 */
	private ApiProperties api = new ApiProperties();

	/** redis缓存配置 */
	private CacheProperties cache = new CacheProperties();

	/** 文件相关配置 */
	private FileinfoProperties fileinfo = new FileinfoProperties();

	/** rabbitmq配置 */
	private RabbitMqProperties rabbitmq = new RabbitMqProperties();

	/** 所有拦截器参数 */
	private FilterProperties filter = new FilterProperties();
}