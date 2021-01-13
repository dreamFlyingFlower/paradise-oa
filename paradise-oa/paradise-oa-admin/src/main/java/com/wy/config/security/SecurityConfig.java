package com.wy.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wy.filter.JwtAuthenticationTokenFilter;
import com.wy.properties.ConfigProperties;

/**
 * springsecurity配置,加上prePostEnabled和securedEnabled才可以在项目中的类上添加PreAuthorize注解
 * 
 * @author ParadiseWY
 * @date 2020年4月2日 下午11:09:37
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/** 自定义实现登录认证 */
	@Autowired
	private LoginAuthenticationProvider loginAuthenticationProvider;

	/** 自定义认证成功处理 */
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	/** 自定义认证失败处理 */
	@Autowired
	private LoginFailureHandler loginFailureHandler;

	/** 自定义退出处理 */
	@Autowired
	private LogoutSuccessHandlerImpl logoutSuccessHandler;

	/** token认证过滤器 */
	@Autowired
	private JwtAuthenticationTokenFilter authenticationTokenFilter;

	/** 自定义登录拦截属性 */
	@Autowired
	private ConfigProperties config;

	/**
	 * 身份认证接口,登录时调用
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(loginAuthenticationProvider);
	}

	/**
	 * security的url拦截,登录请求必须是post
	 * 
	 * anyRequest:匹配所有请求路径,access:SpringEl表达式结果为true时可以访问,anonymous:匿名可以访问,denyAll:用户不能访问
	 * fullyAuthenticated:用户完全认证可以访问(非remember-me下自动登录),hasAnyAuthority:如果有参数,参数表示权限,则其中任何一个权限可以访问
	 * hasAnyRole:如果有参数,参数表示角色,则其中任何一个角色可以访问 hasAuthority:如果有参数,参数表示权限,则其权限可以访问
	 * hasIpAddress:如果有参数,参数表示IP地址,如果用户IP和参数匹配,则可以访问,hasRole:如果有参数,参数表示角色,则其角色可以访问,
	 * permitAll:用户可以任意访问,rememberMe:允许通过remember-me登录的用户访问 authenticated:用户登录后可访问
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// CRSF禁用
				.csrf().disable().headers().frameOptions().disable().and()
				// 添加JWT拦截
				.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
				// 认证失败处理类
//				 .exceptionHandling().authenticationEntryPoint(loginFailureHandle).and()
				.exceptionHandling().authenticationEntryPoint(new SecurityEntryPoint(null)).and()
				// 基于token,所以不需要session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				// 过滤请求
				.authorizeRequests()
				// 不进行拦截的url
				.antMatchers(
						config.getApi().getExcludeApis().toArray(new String[config.getApi().getExcludeApis().size()]))
				.permitAll()
				// 除上面外的所有请求全部需要鉴权认证
				.anyRequest().authenticated()
				// 自定义登录接口,默认为/login,自定义登录时的用户名,默认为username
				.and().formLogin().loginProcessingUrl("/user/login").usernameParameter("account")
				// 自定义登录成功和失败处理方法
				.successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
				// 自定义登出方法
				.and().logout().logoutUrl("/user/logout").logoutSuccessHandler(logoutSuccessHandler);
	}
}