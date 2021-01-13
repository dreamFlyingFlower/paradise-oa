package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.UserRole;

import io.swagger.annotations.Api;

/**
 * 用户角色中间表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "用户角色中间表API")
@RestController
@RequestMapping("userRole")
public class UserRoleCrl extends AbstractCrl<UserRole, Long> {

}