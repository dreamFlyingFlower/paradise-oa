package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.LoginLog;
import com.wy.result.Result;

/**
 * 用户登录日志
 * 
 * @author 飞花梦影
 * @date 2021-01-13 16:08:59
 * @git {@link https://github.com/mygodness100}
 */
public interface LoginLogService extends BaseService<LoginLog, Long> {

	/**
	 * 查询系统登录日志集合
	 * 
	 * @param logininfor 访问日志对象
	 * @return 登录记录集合
	 */
	Result<List<LoginLog>> selectLogininforList(LoginLog logininfor);
}