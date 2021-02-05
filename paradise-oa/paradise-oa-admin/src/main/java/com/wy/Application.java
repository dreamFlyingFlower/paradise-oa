package com.wy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 * 
 * FIXME 加班,搭建自己的邮件服务器
 * 
 * 参照:https://gitee.com/aaluoxiang/oa_system
 * 	
 *	@author 飞花梦影
 *	@date 2021-01-13 12:00:32
 * @git {@link https://github.com/mygodness100}
 */
@EnableCaching
@EnableAsync
@MapperScan("com.wy.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}