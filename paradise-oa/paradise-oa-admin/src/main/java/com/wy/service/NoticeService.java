package com.wy.service;

import com.wy.base.BaseService;
import com.wy.model.Notice;

/**
 * 公告表
 * 
 * @author 飞花梦影
 * @date 2021-02-08 16:14:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface NoticeService extends BaseService<Notice, Long> {

	/**
	 * 修改通知为已读状态
	 * 
	 * @param noticeId 通知编号
	 * @param userId 用户编号
	 * @return 影响行数
	 */
	int read(Long noticeId, Long userId);
}