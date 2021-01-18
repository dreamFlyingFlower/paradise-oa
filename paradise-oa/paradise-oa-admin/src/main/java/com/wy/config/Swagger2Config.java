package com.wy.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.base.AbstractPager;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 使用swagger2自动生成文档,文档查看地址http://ip:port/swagger-ui.html#/
 * 	
 *	@author 飞花梦影
 *	@date 2019-06-10 22:22:53
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

	/**
	 * 配置swagger2文档相关信息
	 * 
	 * @return 实例
	 */
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).pathMapping("api").groupName("通用文档").apiInfo(apiInfo()).select()
				/** 扫描所有有注解的api */
				// .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				/** 扫描指定包中所有有注解的 */
				.apis(RequestHandlerSelectors.basePackage("com.wy.crl"))
				/** 扫描所有包 */
				// .apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build()
				/** 设置安全模式，swagger可以设置访问token */
				.securitySchemes(securitySchemes()).securityContexts(securityContexts())
				/** 忽略不需要进行扫描的类 */
				.ignoredParameterTypes(AbstractPager.class);
	}

	/**
	 * @apiNote 添加摘要信息
	 * @return 摘要信息
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("接口文档").description("通用接口文档").version("1.0").build();
	}

	/**
	 * @apiNote 安全模式,这里指定token通过Authorization头请求头传递
	 */
	private List<ApiKey> securitySchemes() {
		List<ApiKey> apiKeyList = new ArrayList<ApiKey>();
		apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
		return apiKeyList;
	}

	/**
	 * @apiNote 安全上下文
	 */
	private List<SecurityContext> securityContexts() {
		List<SecurityContext> securityContexts = new ArrayList<>();
		securityContexts.add(SecurityContext.builder().securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex("^(?!auth).*$")).build());
		return securityContexts;
	}

	/**
	 * @apiNote 默认的安全上引用
	 */
	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		List<SecurityReference> securityReferences = new ArrayList<>();
		securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
		return securityReferences;
	}
}