package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Notice;
import com.wy.result.Result;
import com.wy.service.NoticeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 公告表API
 * 
 * @author 飞花梦影
 * @date 2021-02-08 16:14:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "公告表API")
@RestController
@RequestMapping("notice")
public class NoticeCrl extends AbstractCrl<Notice, Long> {

	@Autowired
	private NoticeService noticeService;

	@ApiOperation("修改通知为已读")
	@GetMapping("read/{noticeId}")
	public Result<?> read(@ApiParam("通知编号") @PathVariable Long noticeId, @ApiParam("用户编号") Long userId) {
		return Result.result(noticeService.read(noticeId, userId) > 0);
	}
}