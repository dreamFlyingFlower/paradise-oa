package com.wy.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件上传相关参数
 * 
 * @author 飞花梦影
 * @date 2021-01-14 15:35:02
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class FileinfoProperties {

	/** 上传的文件名最大长度,需要根据数据库修改 */
	private int fileNameLength = 64;

	/** 上传文件本地存储地址 */
	private String fileLocal = "E:\\repository\\gitee\\work\\SimpleOA\\src\\main\\resources\\static\\upload";

	/** 服务器访问本地文件地址;不在数据库中存储http地址,防止http变动文件访问不了 */
	private String fileHttp = "http://192.168.1.170:12345/upload";

	/** 上传或下载文件目录,非本系统资源目录 */
	private String profile;

	/** 获取头像上传路径 */
	private String avatarPath = profile + "avatar";

	/** 获取下载路径 */
	private String downloadPath = profile + "download";

	/** 获取上传路径 */
	private String uploadPath = profile + "upload";
}