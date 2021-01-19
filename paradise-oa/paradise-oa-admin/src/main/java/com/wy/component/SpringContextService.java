package com.wy.component;

import java.util.Map;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring上下文工具类,可在非spring组件中使用spring组件
 * 
 *	@author 飞花梦影
 *	@date 2021-01-14 15:44:03
 * @git {@link https://github.com/mygodness100}
 */
@Component
public final class SpringContextService implements InitializingBean, ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextService.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public ApplicationContext getContext() {
		return applicationContext;
	}

	/**
	 * @apiNote 根据bean的beanName来查找对象
	 * @param beanName spring组件中定义的value,若是不写,默认为类名首字母小写
	 * @return 对象,需强转
	 */
	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	/**
	 * @apiNote 根据bean的class来查找对象
	 * @param clazz 需要查找的类字节码
	 * @return 对象
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	/**
	 * @apiNote 根据bean的class来查找所有的对象(包括子类)
	 * @param clazz 父类字节码
	 * @return 所有子类
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
		return applicationContext.getBeansOfType(clazz);
	}

	/**
	 * @apiNote 如果context中包含一个与所给名称匹配的bean定义则返回true
	 * @param beanName spring组件的name
	 * @return boolean 是否存在
	 */
	public static boolean containsBean(String beanName) {
		return applicationContext.containsBean(beanName);
	}

	/**
	 * @apiNote 判断以给定名字注册的bean定义是一个singleton还是一个prototype
	 * @param beanName spring组件的name
	 * @return boolean 单例则返回true,否则返回false
	 */
	public static boolean isSingleton(String beanName) {
		return applicationContext.isSingleton(beanName);
	}

	/**
	 * @apiNote 根据beanName返回该组件的具体字节码类
	 * @param beanName spring组件的name
	 * @return Class 注册对象的类型
	 */
	public static Class<?> getType(String beanName) {
		return applicationContext.getType(beanName);
	}

	/**
	 * @apiNote 获取aop代理对象
	 * @param invoker
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAopProxy(T invoker) {
		return (T) AopContext.currentProxy();
	}
}