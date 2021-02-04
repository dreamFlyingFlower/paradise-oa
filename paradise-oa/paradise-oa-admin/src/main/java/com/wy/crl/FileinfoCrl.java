package com.wy.crl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import io.swagger.annotations.ApiParam;

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
	 * 文件上传
	 */
	@ApiOperation("文件上传")
	@PostMapping("upload")
	public Result<?> upload(MultipartFile file) {
		return Result.ok(fileinfoService.upload(file));
	}

	/**
	 * 批量文件上传
	 */
	@ApiOperation("批量文件上传")
	@PostMapping("uploads")
	public Result<?> uploads(MultipartFile[] files) {
		return Result.ok(fileinfoService.uploads(files));
	}

	/**
	 * 根据文件本地完整名称删除文件
	 */
	@ApiOperation("根据文件本地完整名称删除文件")
	@PostMapping("deleteByLocalName/{localName}")
	public Result<?> deleteByLocalName(@PathVariable String localName) {
		return Result.ok(fileinfoService.deleteByLocalName(localName));
	}

	/**
	 * 根据文件本地完整名称批量删除文件
	 */
	@ApiOperation("根据文件本地完整名称批量删除文件")
	@PostMapping("deleteByLocalNames")
	public Result<?> deleteByLocalNames(@RequestBody List<String> localNames) {
		return Result.ok(fileinfoService.deleteByLocalNames(localNames));
	}

	/**
	 * 根据文件编号下载
	 * 
	 * @param fileId 文件编号
	 */
	@ApiOperation("文件下载")
	@GetMapping("download/{fileId}")
	public void download(@ApiParam("文件编号") @PathVariable Long fileId, HttpServletResponse response,
			HttpServletRequest request) {
		Fileinfo fileinfo = (Fileinfo) fileinfoService.getById(fileId);
		if (Objects.isNull(fileinfo)) {
			throw new ResultException(TipEnum.TIP_FILE_NOT_EXIST);
		}
		handler(response, fileinfo);
	}

	/**
	 * 根据文件完整本地名称下载
	 * 
	 * @param localName 文件完整本地名称
	 */
	@ApiOperation("文件下载")
	@GetMapping("downloadByLocalName")
	public void downloadByLocalName(@ApiParam("文件完整本地名称") String localName, HttpServletResponse response,
			HttpServletRequest request) {
		Result<List<Fileinfo>> entitys = fileinfoService.getEntitys(Fileinfo.builder().localName(localName).build());
		if (ListUtils.isBlank(entitys.getData())) {
			throw new ResultException(TipEnum.TIP_FILE_NOT_EXIST);
		}
		handler(response, entitys.getData().get(0));
	}

	private void handler(HttpServletResponse response, Fileinfo fileinfo) {
		response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		// 二选一即可,暂时还不知道区别 FIXME
		response.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
		// response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		// 若中文乱码,需要将文件名字符集改为ISO_8859_1
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileinfo.getOriginalName());
		try {
			Files.copy(FilesUtils.getLocalPath(fileinfo.getLocalName()), response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResultException(TipEnum.TIP_FILE_DOWNLOAD_FAILED);
		}
	}
}