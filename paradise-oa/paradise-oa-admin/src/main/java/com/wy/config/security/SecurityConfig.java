package com.wy.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wy.filter.TokenFilter;
import com.wy.properties.ConfigProperties;

/**
 * springsecurity配置,prePostEnabled为true才可以使{@link PreAuthorize},{@link PostAuthorize}生效
 * securedEnabled为true可以使{@link Secured @Secured}生效
 * 
 * @author 飞花梦影
 * @date 2021-02-04 22:36:48
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoginAuthenticationProvider loginAuthenticationProvider;

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Autowired
	private LoginFailureHandler loginFailureHandler;

	@Autowired
	private LogoutSuccessHandlerImpl logoutSuccessHandler;

	@Autowired
	private TokenFilter tokenFilter;

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
	 * authorizeRequests:开始请求权限配置<br>
	 * antMatchers:表示所有匹配的请求,这里是资源文件目录,不可写/resources/**或/static/**,仍然会拦截<br>
	 * anyRequest:匹配所有请求路径<br>
	 * access:SpringEl表达式结果为true时可以访问<br>
	 * permitAll:用户可以任意访问<br>
	 * anonymous:匿名可以访问<br>
	 * authenticated:用户登录后可访问<br>
	 * denyAll:用户不能访问<br>
	 * fullyAuthenticated:用户完全认证可以访问(非remember-me下自动登录)<br>
	 * hasAnyAuthority:如果有参数,参数表示权限,则其中任何一个权限可以访问<br>
	 * hasAuthority:如果有参数,参数表示权限,则其权限可以访问<br>
	 * hasAnyRole:如果有参数,参数表示角色,则其中任何一个角色可以访问<br>
	 * hasIpAddress:如果有参数,参数表示IP地址,如果用户IP和参数匹配,则可以访问<br>
	 * hasRole:如果有参数,参数表示角色,则其角色可以访问<br>
	 * rememberMe:允许通过remember-me登录的用户访问<br>
	 * 
	 * formlogin:允许表单登录<br>
	 * httpbasic:允许http请求登录<br>
	 * loginPage:拦截未登录的请求到指定页面,只能是内置的页面,默认/login<br>
	 * loginProcessingUrl:自定义的前端登录请求url,注意开头必须有/<br>
	 * usernameParameter:自定义用户名的请求字段,默认username.可写在配置文件中<br>
	 * passwordParameter:自定义密码的请求字段,默认password,可写在配置文件中<br>
	 * successHandler:登录成功后的处理方法,要实现AuthenticationFailureHandler或重写其子类<br>
	 * failureHandler:登录失败后的处理方法,要实现AuthenticationSuccessHandler或重写其子类<br>
	 * exceptionHandling:异常处理<br>
	 * authenticationEntryPoint:开始一个验证的时候,验证失败的时候不跳到默认的登录界面<br>
	 * AuthLoginConfig:自定义的未登录拦截类,不跳转到默认的未登录页面,而是自定义返回json<br>
	 * csrf:禁用csrf防御机制,可跨域请求<br>
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// CRSF禁用
				.csrf().disable().headers().frameOptions().disable().and()
				// 认证失败处理类,若不定义则跳转到loginForm自定义地址或默认的/login
				// .exceptionHandling().authenticationEntryPoint(loginFailureHandle).and()
				.exceptionHandling().authenticationEntryPoint(new SecurityEntryPoint(null)).and()
				// 使用记住密码功能,需要使用数据库,只是服务端记住,客户端也需要记住才可
				// .rememberMe().tokenRepository(persistentTokenRepository())
				// token超时时间,单位s
				// .tokenValiditySeconds(1209600)
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
				.and().logout().clearAuthentication(true).logoutUrl("/user/logout")
				.logoutSuccessHandler(logoutSuccessHandler);
		// 添加token拦截,若使用token就不使用session;若不使用token,就使用session
		if (config.getFilter().isTokenEnable()) {
			httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
			httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
	}

	/**
	 * 自定义数据库中的token实现,当有记住我的时候,将会将token存入数据库
	 */
	// @Bean
	// public PersistentTokenRepository persistentTokenRepository() {
	// JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
	// tokenRepository.setDataSource(datasource);
	// return tokenRepository;
	// }
}