package com.wy.system.service.impl;

import java.util.List;

import com.wy.model.Cpu;
import com.wy.model.Jvm;
import com.wy.model.Mem;
import com.wy.model.Server;
import com.wy.model.Sys;
import com.wy.model.SysFile;
import com.wy.util.SystemUtils;

/**
 * 系统相关信息
 * 
 * @author 飞花梦影
 * @date 2021-01-13 15:30:15
 * @git {@link https://github.com/mygodness100}
 */
public class SystemServiceImpl {

	public Cpu getCpu() {
		return SystemUtils.getCpuinfo();
	}

	public Jvm getJvm() {
		return SystemUtils.getJvminfo();
	}

	public Mem getMem() {
		return SystemUtils.getMeminfo();
	}

	public Sys getSys() {
		return SystemUtils.getSysinfo();
	}

	public List<SysFile> getSysFiles() {
		return SystemUtils.getSysFilesinfo();
	}

	public Server getServer() {
		return SystemUtils.getServer();
	}
}