package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.RoleDepart;

import io.swagger.annotations.Api;

/**
 * 角色部门关联表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 11:01:35
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "角色部门关联表API")
@RestController
@RequestMapping("roleDepart")
public class RoleDepartCrl extends AbstractCrl<RoleDepart, Long> {

}