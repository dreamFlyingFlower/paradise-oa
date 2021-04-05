package com.wy.model;

import java.lang.management.ManagementFactory;

import com.wy.lang.NumberTool;
import com.wy.util.DateTool;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * JVM相关信息
 * 
 * @author 飞花梦影
 * @date 2021-01-13 15:28:07
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "JVM相关信息")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Jvm {

	/**
	 * 当前JVM占用的内存总数(M)
	 */
	private double total;

	/**
	 * JVM最大可用内存总数(M)
	 */
	private double max;

	/**
	 * JVM空闲内存(M)
	 */
	private double free;

	/**
	 * JDK版本
	 */
	private String version;

	/**
	 * JDK路径
	 */
	private String home;

	public double getTotal() {
		return NumberTool.div(total, (1024 * 1024), 2).doubleValue();
	}

	public double getMax() {
		return NumberTool.div(max, (1024 * 1024), 2).doubleValue();
	}

	public double getFree() {
		return NumberTool.div(free, (1024 * 1024), 2).doubleValue();
	}

	public double getUsed() {
		return NumberTool.div(total - free, (1024 * 1024), 2).doubleValue();
	}

	public double getUsage() {
		return NumberTool.multiply(NumberTool.div(total - free, total, 4), 100).doubleValue();
	}

	/**
	 * 获取JDK名称
	 */
	public String getName() {
		return ManagementFactory.getRuntimeMXBean().getVmName();
	}

	/**
	 * JDK启动时间
	 */
	public String getStartTime() {
		return DateTool.formatDateTime(ManagementFactory.getRuntimeMXBean().getStartTime());
	}

	/**
	 * JDK运行时间
	 */
	public String getRunTime() {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = ManagementFactory.getRuntimeMXBean().getStartTime() - System.currentTimeMillis();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}
}