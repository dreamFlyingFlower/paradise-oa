package com.wy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.mapper.UserNoticeMapper;
import com.wy.model.Notice;
import com.wy.model.UserNotice;
import com.wy.service.NoticeService;

/**
 * 公告表
 * 
 * @author 飞花梦影
 * @date 2021-02-08 16:14:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("noticeService")
public class NoticeServiceImpl extends AbstractService<Notice, Long> implements NoticeService {

	@Autowired
	private UserNoticeMapper userNoticeMapper;

	@Override
	public int read(Long noticeId, Long userId) {
		return userNoticeMapper
				.updateByPrimaryKey(UserNotice.builder().userId(userId).noticeId(noticeId).read(1).build());
	}
}