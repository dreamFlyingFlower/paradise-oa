package com.wy.aspect;

import java.lang.reflect.Method;
import java.text.MessageFormat;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.wy.annotation.PermissionScope;
import com.wy.config.security.TokenService;
import com.wy.model.Role;
import com.wy.model.User;
import com.wy.util.spring.ContextUtils;
import com.wy.util.spring.ServletUtils;
import com.wy.utils.StrUtils;

/**
 * @apiNote 数据过滤处理 FIXME
 * @author ParadiseWY
 * @date 2020年4月3日 上午9:32:57
 */
@Aspect
@Component
public class DataScopeAspect {
	/**
	 * 全部数据权限
	 */
	public static final String DATA_SCOPE_ALL = "1";

	/**
	 * 自定数据权限
	 */
	public static final String DATA_SCOPE_CUSTOM = "2";

	/**
	 * 部门数据权限
	 */
	public static final String DATA_SCOPE_DEPT = "3";

	/**
	 * 部门及以下数据权限
	 */
	public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

	/**
	 * 仅本人数据权限
	 */
	public static final String DATA_SCOPE_SELF = "5";

	// 配置织入点
	@Pointcut("@annotation(com.wy.annotation.PermissionScope)")
	public void dataScopePointCut() {
	}

	@Before("dataScopePointCut()")
	public void doBefore(JoinPoint point) throws Throwable {
		handleDataScope(point);
	}

	protected void handleDataScope(final JoinPoint joinPoint) {
		// 获得注解
		PermissionScope controllerDataScope = getAnnotationLog(joinPoint);
		if (controllerDataScope == null) {
			return;
		}
		// 获取当前的用户
		User currentUser = ContextUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getHttpServletRequest());
		if (currentUser != null) {
			// 如果是超级管理员，则不过滤数据
			if (currentUser.getUserId().intValue() != 1) {
				dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
						controllerDataScope.userAlias());
			}
		}
	}

	/**
	 * 数据范围过滤
	 * 
	 * @param joinPoint 切点
	 * @param user 用户
	 * @param alias 别名
	 */
	public static void dataScopeFilter(JoinPoint joinPoint, User user, String deptAlias, String userAlias) {
		StringBuilder sqlString = new StringBuilder();
		for (Role role : user.getRoles()) {
			String dataScope = role.getPermission();
			if (DATA_SCOPE_ALL.equals(dataScope)) {
				sqlString = new StringBuilder();
				break;
			} else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
				sqlString.append(MessageFormat.format(
						" OR {0}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {1} ) ", deptAlias,
						role.getRoleId()));
			} else if (DATA_SCOPE_DEPT.equals(dataScope)) {
				sqlString.append(MessageFormat.format(" OR {0}.dept_id = {1} ", deptAlias, user.getDepartId()));
			} else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
				sqlString.append(MessageFormat.format(
						" OR {0}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {1} or find_in_set( {2} , ancestors ) )",
						deptAlias, user.getDepartId(), user.getDepartId()));
			} else if (DATA_SCOPE_SELF.equals(dataScope)) {
				if (StrUtils.isNotBlank(userAlias)) {
					sqlString.append(MessageFormat.format(" OR {0}.user_id = {1} ", userAlias, user.getUserId()));
				} else {
					// 数据权限为仅本人且没有userAlias别名不查询任何数据
					sqlString.append(" OR 1=0 ");
				}
			}
		}

		if (StrUtils.isNotBlank(sqlString.toString())) {
			// FIXME
			// AbstractModel abstractModel = (AbstractModel) joinPoint.getArgs()[0];
			// abstractModel.setDataScope(" AND (" + sqlString.substring(4) + ")");
		}
	}

	/**
	 * 是否存在注解，如果存在就获取
	 */
	private PermissionScope getAnnotationLog(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();

		if (method != null) {
			return method.getAnnotation(PermissionScope.class);
		}
		return null;
	}
}