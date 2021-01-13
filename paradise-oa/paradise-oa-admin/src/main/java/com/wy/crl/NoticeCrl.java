package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Notice;
import com.wy.result.Result;
import com.wy.service.NoticeService;

import io.swagger.annotations.Api;

/**
 * 公告表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "公告表API")
@RestController
@RequestMapping("notice")
public class NoticeCrl extends AbstractCrl<Notice, Long> {

	@Autowired
	private NoticeService noticeService;

	/**
	 * 获取通知公告列表
	 */
	@PreAuthorize("@ss.hasPermi('system:notice:list')")
	@GetMapping("/list")
	public Result<?> list(Notice notice) {
		return noticeService.selectNoticeList(notice);
	}
}