package com.wy.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.common.Constants;
import com.wy.enums.TipEnum;
import com.wy.mapper.RoleMapper;
import com.wy.model.Role;
import com.wy.model.RoleExample;
import com.wy.result.ResultException;
import com.wy.service.RoleService;
import com.wy.utils.ListUtils;

/**
 * 角色表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Service("roleService")
public class RoleServiceImpl extends AbstractService<Role, Long> implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	/**
	 * 根据用户编号判断是否为超级管理员,只考虑单角色
	 * 
	 * @param userId 用户编号
	 * @return true是,false否
	 */
	public boolean ifAdminUser(Long userId) {
		List<Role> selectEntitys = roleMapper.selectByUserId(userId);
		if (ListUtils.isBlank(selectEntitys)) {
			throw new ResultException(TipEnum.TIP_USER_NOT_DISTRIBUTE_ROLE);
		}
		return selectEntitys.get(0).getRoleType() == 0;
	}

	/**
	 * 判断是否为超级管理员
	 * 
	 * @param roleId 角色编号
	 * @return true是,false否
	 */
	public boolean ifAdmin(Long roleId) {
		Role role = roleMapper.selectByPrimaryKey(roleId);
		if (null == role) {
			throw new ResultException(TipEnum.TIP_ROLE_ERROR);
		}
		return role.getRoleType() == 0;
	}

	/**
	 * 判断是否为超级管理员
	 * 
	 * @param roleId 角色编号
	 * @return true是,false否
	 */
	public boolean ifAdminType(Integer roleType) {
		return roleType == 0;
	}

	/**
	 * 判断是否为超级管理员
	 * 
	 * @param roleId 角色编号
	 * @return true是,false否
	 */
	public boolean ifAdminType(String roleCode) {
		return roleCode.equals(Constants.SUPER_ADMIN);
	}

	/**
	 * 校验角色是否允许操作
	 * 
	 * @param roleId 角色编号
	 */
	private void assertSuperAdmin(Long roleId) {
		Role role = roleMapper.selectByPrimaryKey(roleId);
		assertSuperAdmin(role);
	}

	/**
	 * 校验角色是否允许操作
	 * 
	 * @param role 角色信息
	 */
	private void assertSuperAdmin(Role role) {
		if (Objects.nonNull(role.getRoleId()) && role.getRoleType() == 0) {
			throw new ResultException("不允许操作超级管理员角色");
		}
	}

	/**
	 * 根据用户ID获取角色选择框列表
	 * 
	 * @param userId 用户ID
	 * @return 角色列表
	 */
	@Override
	public List<Role> getByUserId(Long userId) {
		return roleMapper.selectByUserId(userId);
	}

	/**
	 * 修改保存角色信息
	 * 
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	public int updateSelective(Role role) {
		assertSuperAdmin(role);
		return roleMapper.updateByPrimaryKeySelective(role);
	}

	@Override
	public int delete(Long id) {
		assertSuperAdmin(id);
		return super.delete(id);
	}

	@Override
	public int deletes(List<Long> ids) {
		RoleExample example = new RoleExample();
		example.createCriteria().andRoleIdIn(ids);
		List<Role> roles = roleMapper.selectByExample(example);
		roles.stream().forEach(t -> {
			if (t.getRoleType() == 0) {
				throw new ResultException(TipEnum.TIP_ROLE_NOT_OPERATE_ADMIN);
			}
		});
		return super.deletes(ids);
	}
}