package com.wy.config.quartz;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.wy.model.CronJob;
import com.wy.util.spring.ContextUtils;
import com.wy.utils.ListUtils;
import com.wy.utils.StrUtils;

/**
 * 定时任务执行 FXIME 可使用代理
 *
 * @author ParadiseWY
 * @date 2020年4月7日 下午6:16:23
 */
public class QuartzInvokeHandle {

	/**
	 * 执行方法
	 * 
	 * @param cronJob 系统任务
	 */
	public static void invokeMethod(CronJob cronJob) {
		String invokeTarget = cronJob.getInvokeTarget();
		String beanName = getBeanName(invokeTarget);
		String methodName = getMethodName(invokeTarget);
		List<Object[]> methodParams = getMethodParams(invokeTarget);
		Object bean = null;
		if (!isValidClassName(beanName)) {
			bean = ContextUtils.getBean(beanName);
			invokeMethod(bean, methodName, methodParams);
		} else {
			try {
				bean = Class.forName(beanName).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		invokeMethod(bean, methodName, methodParams);
	}

	/**
	 * 调用任务方法
	 * 
	 * @param bean 目标对象
	 * @param methodName 方法名称
	 * @param methodParams 方法参数
	 */
	private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams) {
		try {
			if (ListUtils.isNotBlank(methodParams)) {
				Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
				method.invoke(bean, getMethodParamsValue(methodParams));
			} else {
				Method method = bean.getClass().getDeclaredMethod(methodName);
				method.invoke(bean);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 校验是否为为class包名
	 * 
	 * @param str 名称
	 * @return true是 false否
	 */
	public static boolean isValidClassName(String invokeTarget) {
		return StrUtils.countMatches(invokeTarget, ".") > 1;
	}

	/**
	 * 获取bean名称
	 * 
	 * @param invokeTarget 目标字符串
	 * @return bean名称
	 */
	public static String getBeanName(String invokeTarget) {
		String beanName = StrUtils.substringBefore(invokeTarget, "(");
		return StrUtils.substringBeforeLast(beanName, ".");
	}

	/**
	 * 获取bean方法
	 * 
	 * @param invokeTarget 目标字符串
	 * @return method方法
	 */
	public static String getMethodName(String invokeTarget) {
		String methodName = StrUtils.substringBefore(invokeTarget, "(");
		return StrUtils.substringAfterLast(methodName, ".");
	}

	/**
	 * 获取method方法参数相关列表
	 * 
	 * @param invokeTarget 目标字符串
	 * @return method方法相关参数列表
	 */
	public static List<Object[]> getMethodParams(String invokeTarget) {
		String methodStr = StrUtils.substringBetween(invokeTarget, "(", ")");
		if (StrUtils.isBlank(methodStr)) {
			return null;
		}
		String[] methodParams = methodStr.split(",");
		List<Object[]> classs = new LinkedList<>();
		for (int i = 0; i < methodParams.length; i++) {
			String str = StrUtils.trimToEmpty(methodParams[i]);
			if (StrUtils.contains(str, "'")) {
				// String字符串类型，包含'
				classs.add(new Object[] { StrUtils.replace(str, "'", ""), String.class });
			} else if (Objects.equals(str, "true") || Objects.equals(str.toLowerCase(), "false")) {
				// boolean布尔类型，等于true或者false
				classs.add(new Object[] { Boolean.valueOf(str), Boolean.class });
			} else if (StrUtils.contains(str, "L")) {
				// long长整形，包含L
				classs.add(new Object[] { Long.valueOf(StrUtils.replaceIgnoreCase(str, "L", "")), Long.class });
			} else if (StrUtils.contains(str, "D")) {
				// double浮点类型，包含D
				classs.add(new Object[] { Double.valueOf(StrUtils.replaceIgnoreCase(str, "D", "")), Double.class });
			} else {
				// 其他类型归类为整形
				classs.add(new Object[] { Integer.valueOf(str), Integer.class });
			}
		}
		return classs;
	}

	/**
	 * 获取参数类型
	 * 
	 * @param methodParams 参数相关列表
	 * @return 参数类型列表
	 */
	public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
		Class<?>[] classs = new Class<?>[methodParams.size()];
		int index = 0;
		for (Object[] os : methodParams) {
			classs[index] = (Class<?>) os[1];
			index++;
		}
		return classs;
	}

	/**
	 * 获取参数值
	 * 
	 * @param methodParams 参数相关列表
	 * @return 参数值列表
	 */
	public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
		Object[] classs = new Object[methodParams.size()];
		int index = 0;
		for (Object[] os : methodParams) {
			classs[index] = (Object) os[0];
			index++;
		}
		return classs;
	}
}