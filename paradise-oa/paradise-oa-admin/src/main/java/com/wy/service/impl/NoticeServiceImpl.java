package com.wy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.wy.base.AbstractService;
import com.wy.mapper.NoticeMapper;
import com.wy.model.Notice;
import com.wy.result.Result;
import com.wy.service.NoticeService;

/**
 * 公告表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Service("noticeService")
public class NoticeServiceImpl extends AbstractService<Notice, Long> implements NoticeService {

	@Autowired
	private NoticeMapper noticeMapper;

	/**
	 * 查询公告列表
	 * 
	 * @param notice 公告信息
	 * @return 公告集合
	 */
	@Override
	public Result<List<Notice>> selectNoticeList(Notice notice) {
		startPage(notice);
		List<Notice> list = noticeMapper.selectEntitys(notice);
		PageInfo<Notice> pageInfo = new PageInfo<>(list);
		return Result.page(list, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
	}
}