package com.wy.crl;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wy.enums.RequestSource;
import com.wy.io.ImageUtils;
import com.wy.properties.ConfigProperties;
import com.wy.result.Result;
import com.wy.result.ResultException;
import com.wy.util.FileUploadUtils;
import com.wy.util.FileUtils;
import com.wy.utils.MapUtils;
import com.wy.utils.StrUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用请求处理,包括验证码,文件上传下载等API
 *
 * @author ParadiseWY
 * @date 2020年4月5日 下午1:04:00
 */
@Api(tags = "通用请求处理,包括验证码,文件上传下载等API")
@Controller
@RequestMapping("common")
@Slf4j
public class CommonCrl {

	@Autowired
	private ConfigProperties config;

	/**
	 * 生成验证码 FIXME
	 * 
	 * @param request 请求,其中必须包含source参数
	 * @param response 响应
	 * @return 成功后返回唯一标识,与验证码对应,登录时可用
	 */
	@ApiOperation("生成验证码")
	@GetMapping("verifyImage")
	@ResponseBody
	public void getCode(@ApiParam("请求,必须包含source参数,该参数有3个值:W表示web,A表示android,I表示ios") HttpServletRequest request,
			HttpServletResponse response) {
		String source = request.getParameter("source");
		if (StrUtils.isBlank(source)) {
			throw new ResultException("请求来源类型为空");
		}
		try {
			// 生成验证码
			String verifyCode = ImageUtils.obtainVerifyImage(response.getOutputStream(),
					ServletRequestUtils.getIntParameter(request, "width", config.getCommon().getVerifyCodeWidth()),
					ServletRequestUtils.getIntParameter(request, "height", config.getCommon().getVerifyCodeHeight()),
					config.getCommon().getVerifyCodeLengh());
			if (Objects.equals(source, RequestSource.WEB.getSource())) {
				// 若是web浏览器,存入session中,通过session存入到redis中
				request.getSession().setAttribute(config.getCache().getIdentifyCodeKey(), verifyCode);
			} else if (Objects.equals(source, RequestSource.ANDROID.getSource())) {
				// 如果是android系统,需要根据打开app时获得的imei存储 FIXME
			} else if (Objects.equals(source, RequestSource.IOS.getSource())) {
				// 如果是ios系统,需要根据什么策略来存储 FIXME
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通用下载请求
	 * 
	 * @param fileName 文件名称
	 * @param delete 是否删除
	 */
	@GetMapping("download")
	public void fileDownload(String fileName, Boolean delete, HttpServletResponse response,
			HttpServletRequest request) {
		try {
			if (!FileUtils.isValidFilename(fileName)) {
				throw new Exception(MessageFormat.format("文件名称({0})非法，不允许下载。 ", fileName));
			}
			String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
			String filePath = config.getCommon().getDownloadPath() + fileName;
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition",
					"attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
			FileUtils.writeBytes(filePath, response.getOutputStream());
			if (delete) {
				FileUtils.deleteFile(filePath);
			}
		} catch (Exception e) {
			log.error("下载文件失败", e);
		}
	}

	/**
	 * 通用上传请求
	 */
	@PostMapping("upload")
	public Result<?> uploadFile(MultipartFile file, HttpServletRequest request) throws Exception {
		try {
			// 上传文件路径
			String filePath = config.getCommon().getUploadPath();
			// 上传并返回新文件名称
			String fileName = FileUploadUtils.upload(filePath, file);
			StringBuffer requestUrl = request.getRequestURL();
			String contextPath = request.getServletContext().getContextPath();
			String url = requestUrl.delete(requestUrl.length() - request.getRequestURI().length(), requestUrl.length())
					.append(contextPath).toString() + fileName;
			Map<String, Object> map = MapUtils.getBuilder("fileName", fileName).add("url", url).build();
			return Result.ok(map);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}

	/**
	 * 本地资源通用下载
	 */
	@GetMapping("download/resource")
	public void resourceDownload(String name, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 本地资源路径
		String localPath = config.getCommon().getProfile();
		// 数据库资源地址
		String downloadPath = localPath + name.substring(name.indexOf(config.getCommon().getResourcesPrefix()));
		// 下载名称
		String downloadName = downloadPath.substring(downloadPath.lastIndexOf("/"));
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition",
				"attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
		FileUtils.writeBytes(downloadPath, response.getOutputStream());
	}
}