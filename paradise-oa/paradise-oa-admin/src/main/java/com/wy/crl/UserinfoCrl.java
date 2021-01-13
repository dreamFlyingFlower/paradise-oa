package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Userinfo;

import io.swagger.annotations.Api;

/**
 * 用户信息扩展表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "用户信息扩展表API")
@RestController
@RequestMapping("userinfo")
public class UserinfoCrl extends AbstractCrl<Userinfo, Long> {

}