package com.wy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wy.base.AbstractService;
import com.wy.mapper.FileinfoMapper;
import com.wy.model.Fileinfo;
import com.wy.model.vo.FileinfoVo;
import com.wy.service.FileinfoService;
import com.wy.util.FilesUtils;

/**
 * 文件表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Service("fileinfoService")
public class FileinfoServiceImpl extends AbstractService<Fileinfo, Long> implements FileinfoService {

	@Autowired
	private FileinfoMapper fileinfoMapper;

	@Override
	public Fileinfo upload(MultipartFile file) {
		FileinfoVo fileinfoVo = FilesUtils.saveFile(file);
		Fileinfo fileinfo = new Fileinfo();
		BeanUtils.copyProperties(fileinfoVo, fileinfo);
		fileinfo.setFileUrl(FilesUtils.getHttpPath(fileinfoVo.getLocalName()));
		fileinfoMapper.insertSelective(fileinfo);
		return fileinfo;
	}
}