package com.wy.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;

import com.wy.utils.StrUtils;

/**
 * XSS(跨站脚本攻击)过滤器
 *
 * @author ParadiseWY
 * @date 2020年4月6日 下午7:48:02
 */
public class XssFilter implements Filter {

	/** 排除链接 */
	private List<String> excludes = new ArrayList<>();

	/** 是否过滤富文本,默认过滤 */
	private boolean includeRichText = true;

	/** 是否开启xss过滤,默认开启 */
	private boolean enabled = true;

	/**
	 * 初始化在FilterConfig中设置的参数
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String tempExcludes = filterConfig.getInitParameter("excludes");
		if (StrUtils.isNotBlank(tempExcludes)) {
			String[] temps = tempExcludes.split(",");
			excludes = Stream.of(temps).filter(t -> StrUtils.isNotBlank(t)).collect(Collectors.toList());
		}
		String includeRichTextStr = filterConfig.getInitParameter("includeRichText");
		if (StrUtils.isNotBlank(includeRichTextStr)) {
			includeRichText = BooleanUtils.toBoolean(includeRichTextStr);
		}
		String tempEnabled = filterConfig.getInitParameter("enabled");
		if (StrUtils.isNotBlank(tempEnabled)) {
			enabled = BooleanUtils.toBoolean(tempEnabled);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		if (handleExcludeURL(req, resp)) {
			chain.doFilter(request, response);
			return;
		}
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request,
				includeRichText);
		chain.doFilter(xssRequest, response);
	}

	private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
		if (!enabled) {
			return true;
		}
		if (excludes == null || excludes.isEmpty()) {
			return false;
		}
		String url = request.getServletPath();
		for (String pattern : excludes) {
			Pattern p = Pattern.compile("^" + pattern);
			Matcher m = p.matcher(url);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {
	}
}