package com.wy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.mapper.RoleMenuMapper;
import com.wy.model.RoleMenu;
import com.wy.model.vo.PermissionVo;
import com.wy.service.RoleMenuService;

/**
 * 角色菜单表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends AbstractService<RoleMenu, Long> implements RoleMenuService {

	@Autowired
	private RoleMenuMapper roleMenuMapper;

	@Override
	public List<PermissionVo> getPermissionByUserId(Long userId) {
		return roleMenuMapper.selectPermissionByUserId(userId);
	}
}