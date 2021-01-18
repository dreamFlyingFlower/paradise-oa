package com.wy.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson.JSON;
import com.wy.enums.TipEnum;
import com.wy.properties.ConfigProperties;
import com.wy.result.ResultException;
import com.wy.utils.ListUtils;
import com.wy.utils.MapUtils;
import com.wy.utils.StrUtils;

/**
 * api拦截器,只有登录和下载资源不需要校验,其他都需要进行校验
 * 
 * @instruction 需要配合redis缓存使用
 * @author paradiseWy
 */
@Order(1)
@Configuration
public class ApiFilter extends OncePerRequestFilter {

	@Autowired
	private ConfigProperties configProperties;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (!configProperties.getApi().isValidApi()) {
			filterChain.doFilter(request, response);
		} else {
			boolean flag = true;
			List<String> notValidApis = configProperties.getApi().getExcludeApis();
			if (ListUtils.isNotBlank(notValidApis)) {
				flag = notValidApis.stream().anyMatch(t -> {
					if (request.getRequestURI().startsWith(t)) {
						try {
							filterChain.doFilter(request, response);
							return false;
						} catch (IOException | ServletException e) {
							e.printStackTrace();
						}
					}
					return true;
				});
			}
			if (flag) {
				apiFilter(request, response, filterChain);
			}
		}
	}

	private void apiFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 从redis缓存中检验是否存在某个值,值从请求头的auth中来
		if (configProperties.getApi().isValidApi()) {
			String auth = request.getHeader("");
			if (StrUtils.isBlank(auth)) {
				throw new ResultException(TipEnum.TIP_AUTH_FAIL);
			}
			Map<Object, Object> entity = redisTemplate.opsForHash().entries(auth);
			if (MapUtils.isNotBlank(entity)) {
				// 处理超时过期的token,需要重新登录,且每次相同的token调用请求,需要将token过期时间后延,1分钟延迟一次
				restoreToken(entity, auth);
				filterChain.doFilter(request, response);
			} else {
				throw new ResultException(TipEnum.TIP_AUTH_FAIL);
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}

	/**
	 * 处理token,间隔一定时间将redis中的过期时间重新设置
	 * 
	 * @param auth token
	 */
	private void restoreToken(Map<Object, Object> entity, String auth) {
		// 间隔时间检查是否需要重新设置redis中的token过期时间为,默认1分钟检查一次
		Long expireTime = redisTemplate.getExpire(auth);
		if (System.currentTimeMillis() - expireTime > configProperties.getApi().getTokenFlushTime()) {
			redisTemplate.restore(auth, JSON.toJSONString(entity).getBytes(),
					configProperties.getApi().getTokenExpireTime(), configProperties.getApi().getTokenTimeUnit());
		}
	}
}