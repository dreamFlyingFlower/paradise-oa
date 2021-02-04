package com.wy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.wy.base.AbstractService;
import com.wy.mapper.FileinfoMapper;
import com.wy.model.Fileinfo;
import com.wy.model.FileinfoExample;
import com.wy.model.vo.FileinfoVo;
import com.wy.service.FileinfoService;
import com.wy.util.FilesUtils;
import com.wy.utils.ListUtils;

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

	@Override
	public List<Fileinfo> uploads(MultipartFile[] files) {
		List<Fileinfo> result = new ArrayList<>();
		for (MultipartFile file : files) {
			Fileinfo fileinfo = upload(file);
			result.add(fileinfo);
		}
		return result;
	}

	@Override
	public int delete(Long fileId) {
		Fileinfo fileinfo = fileinfoMapper.selectByPrimaryKey(fileId);
		if (Objects.isNull(fileinfo)) {
			return 1;
		}
		int row = super.delete(fileId);
		if (row > 0) {
			FilesUtils.delFile(fileinfo.getLocalName());
		}
		return row;
	}

	@Override
	public int deletes(List<Long> ids) {
		int row = 0;
		for (Long id : ids) {
			row += delete(id);
		}
		return row;
	}

	@Transactional
	@Override
	public int deleteByLocalName(String localName) {
		FileinfoExample example = new FileinfoExample();
		example.createCriteria().andLocalNameEqualTo(localName);
		int row = fileinfoMapper.deleteByExample(example);
		if (row > 0) {
			FilesUtils.delFile(localName);
		}
		return row;
	}

	@Transactional
	@Override
	public int deleteByLocalNames(List<String> localNames) {
		if (ListUtils.isBlank(localNames)) {
			return 1;
		}
		int row = 0;
		for (String localName : localNames) {
			row += deleteByLocalName(localName);
		}
		return row;
	}
}