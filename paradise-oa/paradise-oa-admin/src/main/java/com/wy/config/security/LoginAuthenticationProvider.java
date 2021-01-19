package com.wy.config.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import com.wy.crypto.CryptoUtils;
import com.wy.model.User;
import com.wy.properties.ConfigProperties;
import com.wy.result.ResultException;
import com.wy.service.UserService;
import com.wy.util.SecurityUtils;

/**
 * 自定义登录的验证方法,从数据库中取出用户数据封装后返回
 * 
 * @author ParadiseWY
 * @date 2020年4月2日 下午11:33:49
 */
@Configuration
public class LoginAuthenticationProvider implements AuthenticationProvider {

	/** 实现security的UserDetailService接口,验证时会调用loadUserByUsername方法 */
	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private ConfigProperties config;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// 这个获取表单输入中返回的用户名,用户名必须唯一
		String username = authentication.getName();
		// 这里调用我们的自己写的获取用户的方法来判断用户是否存在和密码是否正确
		User user = (User) userService.loadUserByUsername(username);
		if (user == null) {
			throw new BadCredentialsException("用户名不存在");
		}
		// 这个是表单中输入的密码,密码的形式为:加密(密码_当前时间戳)
		String decodePwd = (String) authentication.getCredentials();
		String password = CryptoUtils.AESSimpleCrypt(decodePwd, config.getCommon().getSecretKeyUser(), false);
		password = password.substring(0, password.lastIndexOf("_"));
		if (password.length() > 12) {
			throw new ResultException("密码长度不能超过12位");
		}
		// 使用该加密方式是spring推荐,加密后的长度为60,且被加密的字符串不得超过72
		if (!SecurityUtils.matches(password, user.getPassword())) {
			throw new BadCredentialsException("密码不正确");
		}
		// 设置token
		tokenService.createToken(user);
		// 这里还可以加一些其他信息的判断,比如用户账号已停用等判断,这里为了方便我接下去的判断
		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		// 构建返回的用户登录成功的token
		return new UsernamePasswordAuthenticationToken(user, password, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
}