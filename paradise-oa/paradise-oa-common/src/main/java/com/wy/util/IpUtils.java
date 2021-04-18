package com.wy.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wy.collection.MapTool;
import com.wy.http.HttpTool;
import com.wy.lang.StrTool;

import lombok.extern.slf4j.Slf4j;

/**
 * 根据请求获得ip地址
 * 
 * @author ParadiseWY
 * @date 2020年4月2日 下午11:55:41
 */
@Slf4j
public class IpUtils {

	public static final String IP_ADDRESS_URL = "http://ip.taobao.com/service/getIpInfo.php";

	public static String getIpByRequest(HttpServletRequest request) {
		if (request == null) {
			return "unknown";
		}
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	public static boolean internalIp(String ip) {
		byte[] addr = textToNumericFormatV4(ip);
		return internalIp(addr) || "127.0.0.1".equals(ip);
	}

	private static boolean internalIp(byte[] addr) {
		if (Objects.isNull(addr) || addr.length < 2) {
			return true;
		}
		final byte b0 = addr[0];
		final byte b1 = addr[1];
		// 10.x.x.x/8
		final byte SECTION_1 = 0x0A;
		// 172.16.x.x/12
		final byte SECTION_2 = (byte) 0xAC;
		final byte SECTION_3 = (byte) 0x10;
		final byte SECTION_4 = (byte) 0x1F;
		// 192.168.x.x/16
		final byte SECTION_5 = (byte) 0xC0;
		final byte SECTION_6 = (byte) 0xA8;
		switch (b0) {
			case SECTION_1:
				return true;
			case SECTION_2:
				if (b1 >= SECTION_3 && b1 <= SECTION_4) {
					return true;
				}
			case SECTION_5:
				switch (b1) {
					case SECTION_6:
						return true;
				}
			default:
				return false;
		}
	}

	/**
	 * 将IPv4地址转换成字节
	 * 
	 * @param text IPv4地址
	 * @return byte 字节
	 */
	public static byte[] textToNumericFormatV4(String text) {
		if (text.length() == 0) {
			return null;
		}
		byte[] bytes = new byte[4];
		String[] elements = text.split("\\.", -1);
		try {
			long l;
			int i;
			switch (elements.length) {
				case 1:
					l = Long.parseLong(elements[0]);
					if ((l < 0L) || (l > 4294967295L))
						return null;
					bytes[0] = (byte) (int) (l >> 24 & 0xFF);
					bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
					bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
					bytes[3] = (byte) (int) (l & 0xFF);
					break;
				case 2:
					l = Integer.parseInt(elements[0]);
					if ((l < 0L) || (l > 255L))
						return null;
					bytes[0] = (byte) (int) (l & 0xFF);
					l = Integer.parseInt(elements[1]);
					if ((l < 0L) || (l > 16777215L))
						return null;
					bytes[1] = (byte) (int) (l >> 16 & 0xFF);
					bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
					bytes[3] = (byte) (int) (l & 0xFF);
					break;
				case 3:
					for (i = 0; i < 2; ++i) {
						l = Integer.parseInt(elements[i]);
						if ((l < 0L) || (l > 255L))
							return null;
						bytes[i] = (byte) (int) (l & 0xFF);
					}
					l = Integer.parseInt(elements[2]);
					if ((l < 0L) || (l > 65535L))
						return null;
					bytes[2] = (byte) (int) (l >> 8 & 0xFF);
					bytes[3] = (byte) (int) (l & 0xFF);
					break;
				case 4:
					for (i = 0; i < 4; ++i) {
						l = Integer.parseInt(elements[i]);
						if ((l < 0L) || (l > 255L))
							return null;
						bytes[i] = (byte) (int) (l & 0xFF);
					}
					break;
				default:
					return null;
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return bytes;
	}

	/**
	 * 获得本地的内网ip地址
	 * 
	 * @return 内网ip地址
	 */
	public static String getLocalHostIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
		}
		return "127.0.0.1";
	}

	/**
	 * 获得本地的主机名
	 * 
	 * @return 主机名
	 */
	public static String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return "未知";
	}

	/**
	 * 根据IP地址获取详细的地域信息,这些免费api有次数限制,若频繁调用可存入数据库 FIXME
	 * 淘宝API:http://ip.taobao.com/service/getIpInfo.php?ip=218.192.3.42
	 * 新浪API:http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=218.192.3.42
	 * 或者从该网站下载ip地址:http://software77.net/geo-ip/
	 * 
	 * @param ip ip地址
	 * @param addressOpt 是否根据ip地址获取地域,默认不获取
	 * @return 地域信息
	 */
	public static String getAddressByIp(String ip, boolean addressOpt) {
		String address = "";
		// 内网不查询
		if (internalIp(ip)) {
			return "内网IP";
		}
		if (addressOpt) {
			try {
				String res = HttpTool.sendPost(IP_ADDRESS_URL, JSON.toJSONString(MapTool.builder("ip", ip).build()));
				if (StrTool.isBlank(res)) {
					log.error("获取地理位置异常 {}", ip);
					return address;
				}
				JSONObject obj = JSON.parseObject(res);
				JSONObject data = obj.getObject("data", JSONObject.class);
				address = data.getString("region") + ":" + data.getString("city");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return address;
	}
}