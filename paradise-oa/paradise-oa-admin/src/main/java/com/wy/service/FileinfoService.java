package com.wy.service;

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

	Fileinfo upload(MultipartFile file);
}