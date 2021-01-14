package com.wy.util;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.wy.crypto.CryptoUtils;
import com.wy.properties.ConfigProperties;
import com.wy.result.ResultException;
import com.wy.utils.DateUtils;
import com.wy.utils.StrUtils;

/**
 * 本项目所有上传文件的接口都必须是文件先上传成功,文件上传到本地, 然后将上传成功的文件数据插入到数据库表中,之后再进行其他操作,方便统一管理上传文件
 * 为方便文件备份或转移,上传的文件都以当天文件夹区分,文件命名方式为:32位uuid_yyyyMMdd
 * 
 * @author paradiseWy
 */
public class FilesUtils {

	// 文件名格式化字符串
	private static final String FILE_FORMATTER = "{0}_{1}";

	// 文件后缀名匹配
	private static final Map<Integer, List<String>> FILE_SUFFIXMAP = new HashMap<Integer, List<String>>() {

		private static final long serialVersionUID = 1L;

		{
			put(1, Arrays.asList("BMP", "PNG", "GIF", "JPG", "JPEG"));
			put(2, Arrays.asList("MP4", "AVI", "3GP", "RM", "RMVB", "WMV"));
			put(3, Arrays.asList("AMR", "MP3", "WMA", "WAV", "MID"));
			put(4, Arrays.asList("TXT", "JSON", "XML"));
		}
	};

	/**
	 * 检查文件是否匹配,并返回文件存贮路径
	 * 
	 * @param suffix 后缀名,需要带上点
	 */
	public static int getFileType(String suffix) {
		if (StrUtils.isNotBlank(suffix)) {
			for (Map.Entry<Integer, List<String>> entry : FILE_SUFFIXMAP.entrySet()) {
				if (entry.getValue().contains(suffix.toUpperCase())) {
					return entry.getKey();
				}
			}
		}
		return 5;
	}

	/**
	 * 根据平台获得文件的本地路径,并且每天在基础文件夹下新建当天文件夹,以yyyyMMdd命名
	 * 
	 * @param fileName 文件名
	 * @return 文件路径
	 */
	private static File getNewPath(String fileName) {
		String[] fileNames = Files.getNameWithoutExtension(fileName).split("_");
		File parentFolder = new File(SpringContextUtils.getBean(ConfigProperties.class).getFileinfo().getFileLocal(),
				fileNames[1]);
		if (!parentFolder.exists()) {
			boolean success = parentFolder.mkdir();
			if (!success) {
				throw new ResultException("新建文件的上级文件失败,请重试!");
			}
		}
		return new File(parentFolder, fileName);
	}

	/**
	 * 服务器访问文件地址,在sql查询中使用,防止服务器地址变更
	 * 
	 * @param fileName 文件名
	 * @return 远程访问地址
	 */
	public static String getHttpPath(String fileName) {
		return SpringContextUtils.getBean(ConfigProperties.class).getFileinfo().getFileHttp() + "/" + fileName;
	}

	/**
	 * 保存文件在本地
	 * 
	 * @param file 需要进行存储的文件
	 * @return 存储后的本地的文件名, 带后缀
	 */
	public static String saveFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new ResultException("未获取到文件流");
		}
		String originalFilename = file.getOriginalFilename();
		if (StrUtils.isBlank(originalFilename)) {
			throw new ResultException("文件名不存在, 请检查");
		}
		String extension = Files.getFileExtension(originalFilename);
		String localName = MessageFormat.format(FILE_FORMATTER, CryptoUtils.UUID(),
				DateUtils.format(new Date(), "yyyyMMdd"));
		localName = StrUtils.isBlank(extension) ? localName : MessageFormat.format("{0}.{1}", localName, extension);
		try {
			file.transferTo(getNewPath(localName));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new ResultException("文件服务器处理失败");
		}
		return localName;
	}

	/**
	 * 根据文件名获得文件存储的本地地址
	 * 
	 * @param fileName 带后缀的文件名
	 * @return 本地地址
	 */
	public static File getLocalPath(String fileName) {
		String localName = Files.getNameWithoutExtension(fileName);
		String[] fileNames = localName.split("_");
		if (fileNames.length < 2) {
			throw new ResultException("the pattern of file name is error!");
		}
		return new File(
				new File(new File(SpringContextUtils.getBean(ConfigProperties.class).getFileinfo().getFileLocal()),
						fileNames[1]),
				fileName);
	}

	/**
	 * 计算音视频文件的时长
	 * 
	 * @param seconds 秒数
	 * @return 正常的时分秒格式HH:mm:ss
	 */
	public static String getFileTime(long seconds) {
		long mins = seconds / 60;
		long hour = mins / 60;
		String minutes = mins % 60 > 9 ? mins % 60 + "" : "0" + (mins % 60);
		String hours = hour % 60 > 9 ? hour % 60 + "" : "0" + (hour % 60);
		return hours + ":" + minutes + ":" + (seconds % 60);
	}

	/**
	 * 删除本地文件
	 * 
	 * @param localName 本地存放文件名
	 * @return true表示删除成功, false表示失败
	 */
	public static boolean delFile(String localName) {
		File localFile = getLocalPath(localName);
		if (localFile.exists() && localFile.isFile()) {
			return localFile.delete();
		}
		return false;
	}
}