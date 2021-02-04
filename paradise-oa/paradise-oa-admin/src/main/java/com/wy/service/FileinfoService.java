package com.wy.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.wy.base.BaseService;
import com.wy.model.Fileinfo;

/**
 * 文件表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
public interface FileinfoService extends BaseService<Fileinfo, Long> {

	/**
	 * 单文件上传
	 * 
	 * @param file 文件
	 * @return 文件信息
	 */
	Fileinfo upload(MultipartFile file);

	/**
	 * 批量文件上传
	 * 
	 * @param files 文件列表
	 * @return 文件信息列表
	 */
	List<Fileinfo> uploads(MultipartFile[] files);

	/**
	 * 根据本地文件名删除文件
	 * 
	 * @param localName 本地完整文件名
	 * @return 影响行数
	 */
	int deleteByLocalName(String localName);

	/**
	 * 根据本地文件名批量删除文件
	 * 
	 * @param localNames 本地完整文件名列表
	 * @return 影响行数
	 */
	int deleteByLocalNames(List<String> localNames);
}