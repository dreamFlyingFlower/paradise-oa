package com.wy.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wy.base.AbstractModel;
import com.wy.database.annotation.Pri;
import com.wy.database.annotation.Unique;
import com.wy.enums.SexEnum;
import com.wy.enums.UserState;
import com.wy.excel.annotation.ExcelColumn;
import com.wy.model.vo.PermissionVo;
import com.wy.utils.ListUtils;
import com.wy.valid.ValidEdit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户表 ts_user
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "用户表 ts_user")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractModel implements UserDetails {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	@ExcelColumn(value = "用户序号", prompt = "用户编号")
	@NotNull(groups = ValidEdit.class)
	@Pri
	private Long userId;

	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	@ExcelColumn("登录名称")
	@NotBlank(message = "用户账号不能为空")
	@Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
	@Unique
	private String username;

	/**
	 * 密码,md5加密
	 */
	@ApiModelProperty("密码,md5加密")
	@JsonIgnore
	private String password;

	/**
	 * 真实姓名
	 */
	@ApiModelProperty("真实姓名")
	@ExcelColumn("用户名称")
	@Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
	private String realname;

	/**
	 * 部门编号
	 */
	@ApiModelProperty("部门编号")
	private Long departId;

	/**
	 * 身份证号
	 */
	@ApiModelProperty("身份证号")
	private String idcard;

	/**
	 * 性别,见ts_dict表SEX
	 */
	@ApiModelProperty("性别,见ts_dict表SEX")
	@ExcelColumn(value = "用户性别", propConverter = SexEnum.class)
	private String sex;

	/**
	 * 年龄
	 */
	@ApiModelProperty("年龄")
	private Integer age;

	/**
	 * 住址
	 */
	@ApiModelProperty("住址")
	private String address;

	/**
	 * 出生日期
	 */
	@ApiModelProperty("出生日期")
	private Date birthday;

	/**
	 * 邮箱地址
	 */
	@ApiModelProperty("邮箱地址")
	@ExcelColumn("用户邮箱")
	@Email(message = "邮箱格式不正确")
	@Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
	@Unique
	private String email;

	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	@ExcelColumn("手机号码")
	@Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
	@Unique
	private String mobile;

	/**
	 * 用户状态:0黑名单;默认1正常;2休假;3离职中;4离职
	 */
	@ApiModelProperty("用户状态:0黑名单;默认1正常;2锁定;3休假;4离职中;5离职;6逻辑删除")
	@ExcelColumn(value = "帐号状态", propConverter = UserState.class)
	private Integer state;

	/**
	 * 用户头像
	 */
	@ApiModelProperty("用户头像")
	private String avatar;

	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createtime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty("修改时间")
	private Date updatetime;

	/** 非数据库字段 */
	/**
	 * 部门对象
	 */
	@ApiModelProperty("部门对象")
	private Depart depart;

	/**
	 * 用户扩展信息
	 */
	@ApiModelProperty("用户扩展信息")
	private Userinfo userinfo;

	/**
	 * 用户登录成功后返回的token
	 */
	@ApiModelProperty("用户登录成功后返回的token")
	private String token;

	/**
	 * 用户存入token到redis中的时间
	 */
	@ApiModelProperty("用户存入token到redis中的时间")
	private Date loginTime;

	/**
	 * 用户角色对象
	 */
	@ApiModelProperty("用户角色对象")
	private List<Role> roles;

	/**
	 * 角色编号集合
	 */
	@ApiModelProperty("角色编号集合")
	private List<Long> roleIds;

	/**
	 * 权限集合
	 */
	@ApiModelProperty("权限集合")
	private List<PermissionVo> permissionVos;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (ListUtils.isBlank(roles)) {
			return null;
		}
		List<String> roleCodes = roles.stream().map(t -> {
			return "ROLE_" + t.getRoleCode();
		}).collect(Collectors.toList());
		return AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", roleCodes));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}