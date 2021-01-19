package com.wy.crl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.wy.base.AbstractCrl;
import com.wy.enums.TipEnum;
import com.wy.model.Fileinfo;
import com.wy.result.Result;
import com.wy.result.ResultException;
import com.wy.service.FileinfoService;
import com.wy.util.FilesUtils;
import com.wy.utils.ListUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 文件表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "文件表API")
@RestController
@RequestMapping("fileinfo")
public class FileinfoCrl extends AbstractCrl<Fileinfo, Long> {

	@Autowired
	private FileinfoService fileinfoService;

	/**
	 * 通用文件上传
	 */
	@ApiOperation("通用文件上传")
	@PostMapping("upload")
	public Result<?> upload(MultipartFile file) {
		return Result.ok(fileinfoService.upload(file));
	}

	/**
	 * 通用文件下载
	 * 
	 * @param fileName 文件名称
	 * @param delete 是否删除
	 */
	@ApiOperation("通用文件下载")
	@GetMapping("download")
	public void download(String fileName, HttpServletResponse response, HttpServletRequest request) {
		Result<List<Fileinfo>> entitys = fileinfoService.getEntitys(Fileinfo.builder().localName(fileName).build());
		if (ListUtils.isBlank(entitys.getData())) {
			throw new ResultException(TipEnum.TIP_FILE_NOT_EXIST);
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + entitys.getData().get(0).getOriginalName());
		try {
			Files.copy(FilesUtils.getLocalPath(entitys.getData().get(0).getLocalName()), response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResultException(TipEnum.TIP_FILE_DOWNLOAD_FAILED);
		}
	}
}