package com.wy.model;

import com.wy.utils.NumUtils;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * CPU相关信息
 * 
 * @author 飞花梦影
 * @date 2021-01-13 15:21:48
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "CPU相关信息")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Cpu {

	/**
	 * 核心数
	 */
	private int cpuNum;

	/**
	 * CPU总的使用率
	 */
	private double total;

	/**
	 * CPU系统使用率
	 */
	private double sys;

	/**
	 * CPU用户使用率
	 */
	private double used;

	/**
	 * CPU当前等待率
	 */
	private double wait;

	/**
	 * CPU当前空闲率
	 */
	private double free;

	public double getTotal() {
		return NumUtils.round(NumUtils.multiply(total, 100), 2);
	}

	public double getSys() {
		return NumUtils.round(NumUtils.multiply(sys / total, 100), 2);
	}

	public double getUsed() {
		return NumUtils.round(NumUtils.multiply(used / total, 100), 2);
	}

	public double getWait() {
		return NumUtils.round(NumUtils.multiply(wait / total, 100), 2);
	}

	public double getFree() {
		return NumUtils.round(NumUtils.multiply(free / total, 100), 2);
	}
}