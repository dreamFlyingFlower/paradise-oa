package com.wy.util;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.wy.component.SpringContextService;
import com.wy.crypto.CryptoUtils;
import com.wy.enums.DateEnum;
import com.wy.enums.FileType;
import com.wy.enums.TipResult;
import com.wy.lang.NumberTool;
import com.wy.lang.StrTool;
import com.wy.model.vo.FileinfoVo;
import com.wy.properties.ConfigProperties;
import com.wy.result.ResultException;

/**
 * 本项目所有上传文件的接口
 * 
 * 文件先成功上传到本地, 然后将上传成功的文件数据插入到数据库表中,之后再进行其他操作,方便统一管理上传文件
 * 为方便文件备份或转移,上传的文件都以当天文件夹区分,文件命名方式为:32位uuid_yyyyMMdd
 * 
 * @author 飞花梦影
 * @date 2021-01-18 11:34:41
 * @git {@link https://github.com/mygodness100}
 */
public class FilesUtils {

	private static ConfigProperties config = SpringContextService.getBean(ConfigProperties.class);

	/** 文件名格式化字符串 */
	private static final String FILE_FORMATTER = "{0}_{1}";

	/** 文件后缀名匹配 */
	private static final Map<FileType, List<String>> FILE_SUFFIXMAP = new HashMap<FileType, List<String>>() {

		private static final long serialVersionUID = 1L;

		{
			put(FileType.IMAGE, config.getFileinfo().getSuffixImages());
			put(FileType.VIDEO, config.getFileinfo().getSuffixVideos());
			put(FileType.AUDIO, config.getFileinfo().getSuffixAudios());
			put(FileType.TEXT, config.getFileinfo().getSuffixTexts());
			put(FileType.COMPRESS, config.getFileinfo().getSuffixCompresss());
		}
	};

	/**
	 * 保存文件在本地
	 * 
	 * @param file 需要进行存储的文件
	 * @return 存储后的本地的文件名, 带后缀
	 */
	public static FileinfoVo saveFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new ResultException(TipResult.UPLOAD_FILE_STREAM_NOT_FOUND);
		}
		String originalFilename = file.getOriginalFilename();
		if (StrTool.isBlank(originalFilename)) {
			throw new ResultException(TipResult.UPLOAD_FILE_NAME_NOT_EXIST);
		}
		if (originalFilename.length() > config.getFileinfo().getFileNameLength()) {
			throw new ResultException(TipResult.UPLOAD_FILE_NAME_OVER);
		}
		String extension = Files.getFileExtension(originalFilename);
		String localName = MessageFormat.format(FILE_FORMATTER, CryptoUtils.UUID(),
				DateTimeTool.format(new Date(), DateEnum.DATE_NONE.getPattern()));
		localName = StrTool.isBlank(extension) ? localName : MessageFormat.format("{0}.{1}", localName, extension);
		FileinfoVo fileinfoVo = FileinfoVo.builder().originalName(originalFilename).fileSuffix(extension)
				.localName(localName).build();
		buildFileinfo(fileinfoVo, file.getSize());
		try {
			file.transferTo(getNewPath(localName));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new ResultException(TipResult.UPLOAD_FILE_FAILED);
		}
		return fileinfoVo;
	}

	/**
	 * 根据平台获得文件的本地路径,并且每天在基础文件夹下新建当天文件夹,以yyyyMMdd命名
	 * 
	 * @param fileName 文件名
	 * @return 文件路径
	 */
	private static File getNewPath(String fileName) {
		String[] fileNames = Files.getNameWithoutExtension(fileName).split("_");
		File parentFolder = new File(config.getFileinfo().getFileLocal(), fileNames[1]);
		if (!parentFolder.exists()) {
			boolean success = parentFolder.mkdir();
			if (!success) {
				throw new ResultException("新建文件的上级文件失败,请重试!");
			}
		}
		return new File(parentFolder, fileName);
	}

	/**
	 * 文件大小以及时长转换:大小从字节数组转换为M,若是音视频文件,转换为HH:mm:ss的字符串形式
	 * 
	 * @param fileinfoVo 文件信息
	 * @param fileSize 文件大小,字节数组长度
	 */
	public static void buildFileinfo(FileinfoVo fileinfoVo, long fileSize) {
		// 处理文件类型
		fileinfoVo.setFileType(getFileType(fileinfoVo.getFileSuffix()));
		// 处理文件大小
		if (fileSize <= 0) {
			fileinfoVo.setFileSize(0);
		}
		fileinfoVo.setFileSize(NumberTool.div(fileSize, 1024 * 1024, 2).doubleValue());
		// 处理文件时长 FIXME
	}

	/**
	 * 检查文件是否匹配,并返回文件存贮路径
	 * 
	 * @param suffix 后缀名
	 * @return 文件类型
	 */
	public static FileType getFileType(String suffix) {
		if (StrTool.isNotBlank(suffix)) {
			for (Map.Entry<FileType, List<String>> entry : FILE_SUFFIXMAP.entrySet()) {
				if (entry.getValue().contains(suffix.toUpperCase())) {
					return entry.getKey();
				}
			}
		}
		return FileType.OTHER;
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
		return new File(new File(new File(config.getFileinfo().getFileLocal()), fileNames[1]), fileName);
	}

	/**
	 * 根据http地址获得本地文件的存储地址
	 * 
	 * @param httpUrl http地址
	 * @return 本地地址
	 */
	public static File getLocalPathByHttp(String httpUrl) {
		String fileName = httpUrl.substring(httpUrl.indexOf(".") + 1);
		String localName = Files.getNameWithoutExtension(fileName);
		String[] fileNames = localName.split("_");
		if (fileNames.length < 2) {
			throw new ResultException("the pattern of file name is error!");
		}
		return new File(new File(new File(config.getFileinfo().getFileLocal()), fileNames[1]), fileName);
	}

	/**
	 * 服务器访问文件地址,在sql查询中使用,防止服务器地址变更
	 * 
	 * @param fileName 本地文件名
	 * @return 远程访问地址
	 */
	public static String getHttpPath(String fileName) {
		return config.getFileinfo().getFileHttp() + "/" + fileName;
	}

	/**
	 * 删除本地文件
	 * 
	 * @param localName 本地存放文件名,带后缀
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