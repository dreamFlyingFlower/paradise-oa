package com.wy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.wy.base.AbstractService;
import com.wy.mapper.LoginLogMapper;
import com.wy.model.LoginLog;
import com.wy.result.Result;
import com.wy.service.LoginLogService;

/**
 * 用户登录日志
 * 
 * @author 飞花梦影
 * @date 2021-01-13 16:08:59
 * @git {@link https://github.com/mygodness100}
 */
@Service("loginLogService")
public class LoginLogServiceImpl extends AbstractService<LoginLog, Long> implements LoginLogService {

	@Autowired
	private LoginLogMapper loginLogMapper;

	/**
	 * 查询系统登录日志集合
	 * 
	 * @param logininfor 访问日志对象
	 * @return 登录记录集合
	 */
	@Override
	public Result<List<LoginLog>> selectLogininforList(LoginLog logininfor) {
		startPage(logininfor);
		List<LoginLog> list = loginLogMapper.selectEntitys(logininfor);
		PageInfo<LoginLog> pageInfo = new PageInfo<>(list);
		return Result.page(list, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
	}
}