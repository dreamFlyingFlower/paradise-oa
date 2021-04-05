package com.wy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.wy.Constants;
import com.wy.lang.NumberTool;
import com.wy.model.Cpu;
import com.wy.model.Jvm;
import com.wy.model.Mem;
import com.wy.model.Server;
import com.wy.model.Sys;
import com.wy.model.SysFile;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

/**
 * 
 * 
 * @author 飞花梦影
 * @date 2021-01-13 15:34:21
 * @git {@link https://github.com/mygodness100}
 */
public class SystemUtils {

	public static Server getServer() {
		return Server.builder().cpu(getCpuinfo()).jvm(getJvminfo()).mem(getMeminfo()).sys(getSysinfo())
				.sysFiles(getSysFilesinfo()).build();
	}

	/**
	 * CPU信息
	 * 
	 * @return cpu信息
	 */
	public static Cpu getCpuinfo() {
		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();
		long[] prevTicks = hal.getProcessor().getSystemCpuLoadTicks();
		Util.sleep(Constants.OSHI_WAIT_SECOND);
		long[] ticks = hal.getProcessor().getSystemCpuLoadTicks();
		long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
		long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
		long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
		long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
		long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
		long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
		long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
		long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
		long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
		return Cpu.builder().cpuNum(hal.getProcessor().getLogicalProcessorCount()).total(totalCpu).sys(cSys).used(user)
				.free(Double.parseDouble(idle + "")).wait(Double.parseDouble(iowait + "")).build();
	}

	/**
	 * 获得内存信息
	 */
	public static Mem getMeminfo() {
		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();
		return Mem.builder().total(hal.getMemory().getTotal())
				.used(hal.getMemory().getTotal() - hal.getMemory().getAvailable()).free(hal.getMemory().getAvailable())
				.build();
	}

	/**
	 * 获得服务器信息
	 */
	public static Sys getSysinfo() {
		Properties props = System.getProperties();
		return Sys.builder().computerName(IpUtils.getLocalHostName()).computerIp(IpUtils.getLocalHostIp())
				.osName(props.getProperty("os.name")).osArch(props.getProperty("os.arch"))
				.userDir(props.getProperty("user.dir")).build();
	}

	/**
	 * 获得Java虚拟机信息
	 */
	public static Jvm getJvminfo() {
		Properties props = System.getProperties();
		return Jvm.builder().total(Runtime.getRuntime().totalMemory()).max(Runtime.getRuntime().maxMemory())
				.free(Runtime.getRuntime().freeMemory()).version(props.getProperty("java.version"))
				.home(props.getProperty("java.home")).build();
	}

	/**
	 * 获得磁盘信息
	 */
	public static List<SysFile> getSysFilesinfo() {
		SystemInfo si = new SystemInfo();
		OperatingSystem os = si.getOperatingSystem();
		FileSystem fileSystem = os.getFileSystem();
		List<OSFileStore> fsArray = fileSystem.getFileStores();
		ArrayList<SysFile> sysFiles = new ArrayList<SysFile>();
		for (OSFileStore fs : fsArray) {
			long free = fs.getUsableSpace();
			long total = fs.getTotalSpace();
			long used = total - free;
			SysFile sysFile = new SysFile();
			sysFile.setDirName(fs.getMount());
			sysFile.setSysTypeName(fs.getType());
			sysFile.setTypeName(fs.getName());
			sysFile.setTotal(convertFileSize(total));
			sysFile.setFree(convertFileSize(free));
			sysFile.setUsed(convertFileSize(used));
			sysFile.setUsage(NumberTool.multiply(NumberTool.div(used, total, 4), 100).doubleValue());
			sysFiles.add(sysFile);
		}
		return sysFiles;
	}

	/**
	 * 字节转换
	 * 
	 * @param size 字节大小
	 * @return 转换后值
	 */
	public static String convertFileSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;
		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else {
			return String.format("%d B", size);
		}
	}
}