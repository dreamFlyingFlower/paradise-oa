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
	 * 根据用户名或邮件地址,手机号查询用户信息,角色信息
	 * 
	 * @param username 可以是用户名,邮件地址,手机号
	 */
	List<User> selectByUsername(String username);

	/**
	 * 批量查询用户以及角色信息
	 * 
	 * @param ids 用户编号集合
	 * @return 用户以及角色信息
	 */
	List<User> selectUserRoles(List<Long> ids);
}