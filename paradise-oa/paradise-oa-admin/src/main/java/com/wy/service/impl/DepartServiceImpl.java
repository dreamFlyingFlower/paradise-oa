package com.wy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.mapper.DepartMapper;
import com.wy.mapper.UserMapper;
import com.wy.model.Depart;
import com.wy.model.User;
import com.wy.result.ResultException;
import com.wy.service.DepartService;

/**
 * 部门表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Service("departService")
public class DepartServiceImpl extends AbstractService<Depart, Long> implements DepartService {

	@Autowired
	private DepartMapper departMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public int delete(Long id) {
		if (departMapper.countByEntity(Depart.builder().pid(id).build()) > 0) {
			throw new ResultException("存在下级部门,不允许删除");
		}
		if (userMapper.countByEntity(User.builder().departId(id).build()) > 0) {
			throw new ResultException("部门存在用户,不允许删除");
		}
		return super.delete(id);
	}

	@Override
	public int deletes(List<Long> ids) {
		int row = 0;
		for (Long id : ids) {
			row += delete(id);
		}
		return row;
	}
}