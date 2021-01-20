package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.RoleMenu;
import com.wy.model.vo.PermissionVo;

/**
 * 角色菜单表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
public interface RoleMenuService extends BaseService<RoleMenu, Long> {

	/**
	 * 根据用户编号获得获得用户菜单以及菜单权限
	 * 
	 * @param userId 用户编号
	 * @return 菜单以及权限列表
	 */
	List<PermissionVo> getPermissionByUserId(Long userId);
}