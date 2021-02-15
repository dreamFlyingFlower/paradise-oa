package com.wy.crl;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.logger.Log;
import com.wy.logger.LogType;
import com.wy.model.Role;
import com.wy.result.Result;

import io.swagger.annotations.Api;

/**
 * 角色表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "角色表API")
@Secured({ "SUPER_ADMIN", "ADMIN" })
@RestController
@RequestMapping("role")
public class RoleCrl extends AbstractCrl<Role, Long> {

	@Log(value = "角色管理", logType = LogType.INSERT)
	@Override
	public Result<?> create(Role t, BindingResult bind) {
		return super.create(t, bind);
	}

	@Log(value = "角色管理", logType = LogType.INSERT)
	@Override
	public Result<?> creates(List<Role> ts) {
		return super.creates(ts);
	}

	@Log(value = "角色管理", logType = LogType.DELETE)
	@Override
	public Result<?> remove(Long id) {
		return super.remove(id);
	}

	@Log(value = "角色管理", logType = LogType.DELETE)
	@Override
	public Result<?> removes(List<Long> ids) {
		return super.removes(ids);
	}

	@Log(value = "角色管理", logType = LogType.UPDATE)
	@Override
	public Result<?> edit(Role t, BindingResult bind) {
		return super.edit(t, bind);
	}
}