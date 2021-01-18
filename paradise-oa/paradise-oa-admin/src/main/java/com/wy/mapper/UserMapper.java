package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.User;
import com.wy.model.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface UserMapper extends BaseMapper<User, Long> {

	long countByExample(UserExample example);

	int deleteByExample(UserExample example);

	int deleteByPrimaryKey(Long id);

	int insert(User record);

	int insertSelective(User record);

	List<User> selectByExample(UserExample example);

	User selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

	int updateByExample(@Param("record") User record, @Param("example") UserExample example);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	/**
	 * 通过用户名精准查询用户信息
	 * 
	 * @param username 用户名
	 * @return 用户对象信息
	 */
	User selectByUsername(String username);

	/**
	 * 修改用户头像
	 * 
	 * @param userName 用户名
	 * @param avatar 头像地址
	 * @return 结果
	 */
	int updateUserAvatar(@Param("username") String username, @Param("avatar") String avatar);

	/**
	 * 重置用户密码
	 * 
	 * @param userName 用户名
	 * @param password 密码
	 * @return 结果
	 */
	int resetUserPwd(@Param("username") String userName, @Param("password") String password);

	/**
	 * 校验用户名称是否唯一
	 * 
	 * @param userName 用户名称
	 * @return 结果
	 */
	int checkUserNameUnique(String username);

	/**
	 * 校验手机号码是否唯一
	 * 
	 * @param phonenumber 手机号码
	 * @return 结果
	 */
	User checkPhoneUnique(String phonenumber);

	/**
	 * 校验email是否唯一
	 * 
	 * @param email 用户邮箱
	 * @return 结果
	 */
	User checkEmailUnique(String email);
}