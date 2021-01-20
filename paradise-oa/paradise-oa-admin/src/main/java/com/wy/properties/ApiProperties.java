package com.wy.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录时的校验
 * 
 * @author 飞花梦影
 * @date 2021-01-13 10:12:46
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class ApiProperties {

	/** 是否进行api校验,默认不校验 */
	private boolean validApi = false;

	/** 不需要进行校验的api列表 */
	private List<String> excludeApis = new ArrayList<>(Arrays.asList("/login", "/user/login"));

	/** 需要进行校验的api列表,默认null表示全部都要拦截 */
	private List<String> includeApis;
}