package com.wy.model.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 前端vue路由配置信息
 * 
 * @author 飞花梦影
 * @date 2021-01-13 14:03:18
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel("前端vue路由配置信息")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {

	@ApiModelProperty("路由名字")
	private String name;

	@ApiModelProperty("路由地址")
	private String path;

	@ApiModelProperty("是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现")
	private String hidden;

	@ApiModelProperty("重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击")
	private String redirect;

	@ApiModelProperty("组件地址")
	private String component;

	@ApiModelProperty("当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面")
	private Boolean alwaysShow;

	@ApiModelProperty("其他元素")
	private MetaVo meta;

	@ApiModelProperty("子路由")
	private List<RouterVo> children;
}