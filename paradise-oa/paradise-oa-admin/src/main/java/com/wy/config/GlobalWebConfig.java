package com.wy.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;
import com.wy.filter.RepeatRequestFilter;
import com.wy.filter.XssFilter;
import com.wy.intercepter.IdempotencyInterceptor;
import com.wy.properties.ConfigProperties;

/**
 * 拦截器初始化,包含各种拦截器
 *
 * @author ParadiseWY
 * @date 2020年4月6日 下午7:37:26
 */
@Configuration
public class GlobalWebConfig implements WebMvcConfigurer {

	@Autowired
	private ConfigProperties config;

	@Autowired
	private IdempotencyInterceptor idempotencyInterceptor;

	/**
	 * xss过滤器
	 * 
	 * @return 拦截器
	 */
	@Bean
	public FilterRegistrationBean<?> xssFilterRegistration() {
		FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(new XssFilter());
		registration.addUrlPatterns(config.getFilter().getXssUrlPatterns());
		registration.setName("xssFilter");
		registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
		Map<String, String> initParameters = new HashMap<>();
		initParameters.put("excludes", config.getFilter().getXssExcludes());
		initParameters.put("enabled", config.getFilter().getXssEnabled());
		registration.setInitParameters(initParameters);
		return registration;
	}

	/**
	 * 重复请求拦截器
	 * 
	 * @return 拦截器
	 */
	@Bean
	public FilterRegistrationBean<?> sameFilterRegistration() {
		FilterRegistrationBean<RepeatRequestFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new RepeatRequestFilter());
		registration.addUrlPatterns("/*");
		registration.setName("repeatableFilter");
		registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
		return registration;
	}

	/**
	 * 去除监控页面底部的广告
	 */
	@Bean
	@ConditionalOnProperty(name = "spring.datasource.druid.statViewServlet.enabled", havingValue = "true")
	public FilterRegistrationBean<?> removeDruidFilterRegistrationBean(DruidStatProperties properties) {
		// 获取web监控页面的参数
		DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
		// 提取common.js的配置路径
		String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
		String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
		final String filePath = "support/http/resources/js/common.js";
		// 创建filter进行过滤
		Filter filter = new Filter() {

			@Override
			public void init(FilterConfig filterConfig) throws ServletException {}

			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				chain.doFilter(request, response);
				// 重置缓冲区,响应头不会被重置
				response.resetBuffer();
				// 获取common.js
				String text = Utils.readFromResource(filePath);
				// 正则替换banner, 除去底部的广告信息
				text = text.replaceAll("<a.*?banner\"></a><br/>", "");
				text = text.replaceAll("powered.*?shrek.wang</a>", "");
				response.getWriter().write(text);
			}

			@Override
			public void destroy() {}
		};
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(filter);
		registrationBean.addUrlPatterns(commonJsPattern);
		return registrationBean;
	}

	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(idempotencyInterceptor);
	}
}