package com.wy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wy.intercepter.RepeatSubmitInterceptor;
import com.wy.properties.ConfigProperties;

/**
 * 通用配置
 *
 * @author ParadiseWY
 * @date 2020年4月8日 上午12:19:10
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

	@Autowired
	private RepeatSubmitInterceptor repeatSubmitInterceptor;

	@Autowired
	private ConfigProperties config;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/** 本地文件上传路径 */
		registry.addResourceHandler(config.getCommon().getResourcesPrefix() + "/**")
				.addResourceLocations("file:" + config.getCommon().getProfile() + "/");

		/** swagger配置 */
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	/**
	 * 自定义拦截规则
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
	}
}