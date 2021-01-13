package com.wy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wy.enums.DataSourceType;

/**
 * 自定义多数据源切换注解
 *
 * @author ParadiseWY
 * @date 2020年4月8日 上午12:12:10
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {

	/**
	 * 切换数据源名称
	 */
	DataSourceType value() default DataSourceType.MASTER;
}
