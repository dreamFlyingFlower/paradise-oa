package com.wy.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import com.wy.base.BaseService;
import com.wy.model.User;

/**
 * 用户表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
public interface UserService extends BaseService<User, Long>, UserDetailsService {

	// boolean assignRole();
	//
	// void leave();
	//
	// void approve();
	/**
	 * 通过用户名查询用户
	 * 
	 * @param userName 用户名
	 * @return 用户对象信息
	 */
	User selectByUsername(String username);

	/**
	 * 修改用户头像
	 * 
	 * @param file 新上传的文件
	 * @return 用户信息
	 */
	User updateAvatar(MultipartFile file);

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
	 * @param userId 用户编号
	 * @param password 密码
	 * @return 结果
	 */
	int resetUserPwd(Long userId, String password);

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