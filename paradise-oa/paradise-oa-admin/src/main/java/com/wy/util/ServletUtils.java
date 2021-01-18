package com.wy.util;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wy.utils.StrUtils;

/**
 * http请求处理
 * 
 * @author ParadiseWY
 * @date 2020年4月5日 上午11:56:14
 */
public class ServletUtils {

	/**
	 * 获得请求和响应的包裹类
	 * 
	 * @return request和response包裹类
	 */
	public static ServletRequestAttributes getServletRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}

	/**
	 * 获得httpservletrequest类
	 * 
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getHttpServletRequest() {
		return getServletRequestAttributes().getRequest();
	}

	/**
	 * 获得httpservletreponse类
	 * 
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getHttpServletResponse() {
		return getServletRequestAttributes().getResponse();
	}

	/**
	 * 获得request中的header枚举
	 * 
	 * @return request中的header枚举
	 */
	public static Enumeration<String> getHttpServletHeader() {
		return getHttpServletRequest().getHeaderNames();
	}

	/**
	 * 获得request中的header指定key的value
	 * 
	 * @return request中的header指定key的value
	 */
	public static String getHttpServletHeader(String key) {
		return getHttpServletRequest().getHeader(key);
	}

	/**
	 * 获得请求中session类
	 * 
	 * @return HttpSession
	 */
	public static HttpSession getHttpSession() {
		return getHttpServletRequest().getSession();
	}

	/**
	 * 获得session里存储的值
	 * 
	 * @param attributeName 存储的key
	 * @return session中对应的值
	 */
	public static Object getAttribute(String attributeName) {
		return getServletRequestAttributes().getAttribute(attributeName, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * 往session中设置值
	 * 
	 * @param attributeName 存储的key
	 * @param value 存储的值
	 */
	public static void setAttribute(String attributeName, Object value) {
		getServletRequestAttributes().setAttribute(attributeName, value, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * 删除session中的值
	 * 
	 * @param attributeName 存储的key
	 */
	public static void removeAttribute(String attributeName) {
		getServletRequestAttributes().removeAttribute(attributeName, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * 获得请求中的cookie
	 * 
	 * @return cookie数组
	 */
	public static Cookie[] getCookie() {
		return getHttpServletRequest().getCookies();
	}

	/**
	 * 获得请求中的参数
	 * 
	 * @param paramKey 参数key
	 * @return 参数值
	 */
	public static String getParameter(String paramKey) {
		return getHttpServletRequest().getParameter(paramKey);
	}

	/**
	 * 获得请求中的参数
	 * 
	 * @param paramKey 参数key
	 * @param defaultValue 若请求中无值时的默认值
	 * @return 参数值
	 */
	public static String getParameter(String paramKey, String defaultValue) {
		return StrUtils.defaultIfBlank(getHttpServletRequest().getParameter(paramKey), defaultValue);
	}

	/**
	 * 获取Integer参数
	 * 
	 * @param paramKey 参数key
	 * @return 参数值
	 */
	public static Integer getParameterToInt(String paramKey) {
		return NumberUtils.toInt(getHttpServletRequest().getParameter(paramKey));
	}

	/**
	 * 获取Integer参数
	 * 
	 * @param paramKey 参数key
	 * @param defaultValue 默认值
	 * @return 参数值
	 */
	public static Integer getParameterToInt(String paramKey, Integer defaultValue) {
		return NumberUtils.toInt(getHttpServletRequest().getParameter(paramKey), defaultValue);
	}

	/**
	 * 将字符串渲染到客户端
	 * 
	 * @param response 渲染对象
	 * @param result 待渲染的字符串
	 */
	public static void writeResult(HttpServletResponse response, String result) {
		try {
			response.setStatus(200);
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 是否是Ajax请求
	 * 
	 * @param request
	 * @return true是,false否
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String accept = request.getHeader("accept");
		if (accept != null && accept.indexOf("application/json") != -1) {
			return true;
		}
		String xRequestedWith = request.getHeader("X-Requested-With");
		if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
			return true;
		}
		String uri = request.getRequestURI();
		if (StrUtils.contains(uri, ".json") || StrUtils.contains(uri, ".xml")) {
			return true;
		}
		String ajax = request.getParameter("__ajax");
		if (StrUtils.contains(ajax, ".json") || StrUtils.contains(ajax, ".xml")) {
			return true;
		}
		return false;
	}
}