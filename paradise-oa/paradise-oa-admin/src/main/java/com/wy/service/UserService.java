package com.wy.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.wy.base.BaseService;
import com.wy.model.User;
import com.wy.result.Result;

/**
 * 用户表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
public interface UserService extends BaseService<User, Long>, UserDetailsService {
	// User login(String account, String password, String code, String uuid);

	//
	// boolean updateUserIcon(MultipartFile file, Integer userId);
	//
	// boolean updatePwd(int userId, String password);
	//
	// boolean assignRole();
	//
	// void leave();
	//
	// void approve();
	/**
	 * 根据条件分页查询用户列表
	 * 
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	Result<List<User>> selectUserList(User user);

	/**
	 * 通过用户名查询用户
	 * 
	 * @param userName 用户名
	 * @return 用户对象信息
	 */
	User selectByUsername(String username);

	/**
	 * 根据用户ID查询用户所属角色组
	 * 
	 * @param userName 用户名
	 * @return 结果
	 */
	String selectUserRoleGroup(String username);

	/**
	 * 校验用户是否允许操作
	 * 
	 * @param user 用户信息
	 */
	void checkUserAllowed(User user);

	/**
	 * 修改用户状态
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	int updateUserStatus(User user);

	/**
	 * 修改用户基本信息
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	int updateUserProfile(User user);

	/**
	 * 修改用户头像
	 * 
	 * @param userName 用户名
	 * @param avatar 头像地址
	 * @return 结果
	 */
	boolean updateUserAvatar(String username, String avatar);

	/**
	 * 重置用户密码
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	int resetPwd(User user);

	/**
	 * 重置用户密码
	 * 
	 * @param userName 用户名
	 * @param password 密码
	 * @return 结果
	 */
	int resetUserPwd(String username, String password);

	/**
	 * 导入用户数据
	 * 
	 * @param userList 用户数据列表
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 * @param username 操作用户
	 * @return 结果
	 */
	String importUser(List<User> userList, Boolean isUpdateSupport, String username);
}