package com.wy.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.wy.util.JsoupUtils;
import com.wy.utils.StrUtils;

/**
 * xss(跨站脚本攻击),重写请求参数处理,过滤http请求内的参数
 *
 * @author ParadiseWY
 * @date 2020年4月6日 下午7:56:56
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private boolean includeRichText;

	public XssHttpServletRequestWrapper(HttpServletRequest request, boolean includeRichText) {
		super(request);
		this.includeRichText = includeRichText;
	}

	/**
	 * 将参数名和参数值都做xss过滤,若需要拿到原始值,则通过super.getParameterValues来获取
	 */
	@Override
	public String getParameter(String name) {
		Boolean flag = ("content".equals(name) || name.endsWith("WithHtml"));
		if (flag && !includeRichText) {
			return super.getParameter(name);
		}
		String value = super.getParameter(JsoupUtils.clean(name));
		return StrUtils.isNotBlank(value) ? JsoupUtils.clean(value) : value;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values != null && values.length > 0) {
			int length = values.length;
			for (int i = 0; i < length; i++) {
				// 防xss攻击和过滤前后空格
				values[i] = JsoupUtils.clean(values[i]).trim();
			}
		}
		return values;
	}

	/**
	 * 覆盖getHeader方法,将参数名和参数值都做xss过滤, 如果需要获得原始的值,则通过super.getHeaders(name)来获取
	 */
	@Override
	public String getHeader(String name) {
		name = JsoupUtils.clean(name);
		String value = super.getHeader(name);
		if (StrUtils.isNotBlank(value)) {
			value = JsoupUtils.clean(value);
		}
		return value;
	}
}