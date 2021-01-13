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
 * 內存相关信息
 * 
 * @author 飞花梦影
 * @date 2021-01-13 15:28:43
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "內存相关信息")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Mem {

	/**
	 * 内存总量
	 */
	private double total;

	/**
	 * 已用内存
	 */
	private double used;

	/**
	 * 剩余内存
	 */
	private double free;

	public double getTotal() {
		return NumUtils.div(total, (1024 * 1024 * 1024), 2);
	}

	public double getUsed() {
		return NumUtils.div(used, (1024 * 1024 * 1024), 2);
	}

	public double getFree() {
		return NumUtils.div(free, (1024 * 1024 * 1024), 2);
	}

	public double getUsage() {
		return NumUtils.multiply(NumUtils.div(used, total, 4), 100);
	}
}