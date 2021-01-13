package com.wy.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * rabbitmq交换器以及队列配置文件
 * 
 * @author 飞花梦影
 * @date 2021-01-13 10:14:35
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class RabbitMqProperties {

	/**
	 * rabbitmq的邮件交换器名
	 */
	private String exchangeEmail;

	/**
	 * rabbitmq的邮件队列名
	 */
	private String queueEmail;
}