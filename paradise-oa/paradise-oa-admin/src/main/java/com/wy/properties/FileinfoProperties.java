package com.wy.properties;

import java.util.Arrays;
import java.util.List;

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

	/** 图片后缀列表,若重写,需要写全 */
	private List<String> suffixImages = Arrays.asList("BMP", "PNG", "GIF", "JPG", "JPEG");

	/** 视频后缀列表,若重写,需要写全 */
	private List<String> suffixVideos = Arrays.asList("MP4", "AVI", "3GP", "RM", "RMVB", "WMV");

	/** 音频后缀列表,若重写,需要写全 */
	private List<String> suffixAudios = Arrays.asList("AMR", "MP3", "WMA", "WAV", "MID");

	/** 文本后缀列表,若重写,需要写全 */
	private List<String> suffixTexts = Arrays.asList("TXT", "JSON", "XML", "PDF", "DOC", "DOCX", "XLS", "XLSX", "PPT",
			"PPTX");

	/** 压缩包后缀列表,若重写,需要写全 */
	private List<String> suffixCompresss = Arrays.asList("RAR", "ZIP", "GZ", "BZ2", "7Z");
}