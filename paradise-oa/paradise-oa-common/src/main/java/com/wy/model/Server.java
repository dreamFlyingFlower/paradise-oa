package com.wy.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 服务器相关信息
 *
 * @author 飞花梦影
 * @date 2021-01-13 15:26:44
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "服务器相关信息")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Server {

	/**
	 * CPU相关信息
	 */
	private Cpu cpu;

	/**
	 * 內存相关信息
	 */
	private Mem mem;

	/**
	 * JVM相关信息
	 */
	private Jvm jvm;

	/**
	 * 服务器相关信息
	 */
	private Sys sys;

	/**
	 * 磁盘相关信息
	 */
	private List<SysFile> sysFiles;
}