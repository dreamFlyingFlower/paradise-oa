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

	/**
	 * 判断原始密码是否符合条件
	 * 
	 * @param password 前端传到后台的密码:AES加密(原始密码_时间戳)
	 * @return 原始密码
	 */
	String assertPassword(String password);

	/**
	 * 通过用户名或邮件或手机号查询用户
	 * 
	 * @param username 用户名或邮件或手机号
	 * @return 用户对象信息
	 */
	User getByUsername(String username);

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
	 * 用户修改密码
	 * 
	 * @param userId 用户编号
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return 结果
	 */
	int updatePwd(Long userId, String oldPassword, String newPassword);

	/**
	 * 导入用户数据
	 * 
	 * @param userList 用户数据列表
	 * @param username 操作用户
	 * @return 结果
	 */
	int importUser(List<User> userList, String username);

	/**
	 * 用户请假
	 * 
	 * @param userId 用户编号
	 * @param day 请假天数
	 */
	void leave(Long userId, Integer day);

	/**
	 * 批准或驳回请假
	 * 
	 * @param userId 批准人编号
	 * @param taskId 请假的任务编号
	 */
	void approve(Long userId, Long taskId);
}