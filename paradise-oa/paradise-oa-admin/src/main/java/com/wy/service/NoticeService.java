package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.Notice;
import com.wy.result.Result;

/**
 * 公告表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
public interface NoticeService extends BaseService<Notice, Long> {

	/**
	 * 查询公告列表
	 * 
	 * @param notice 公告信息
	 * @return 公告集合
	 */
	Result<List<Notice>> selectNoticeList(Notice notice);
}