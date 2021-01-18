//package com.wy.producer;
//
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.wy.model.vo.EmailVo;
//
///**
// * 广播模式发送生产者
// * 
// * @author ParadiseWY
// * @date 2020/3/18 10:46
// */
//@Component
//public class EmailProducer {
//
//	@Autowired
//	private AmqpTemplate amqpTemplate;
//
//	@Value("${config.rabbitmq.exchange-email}")
//	private String exchageEmail;
//
//	public void sendEmail(EmailVo email) {
//		amqpTemplate.convertAndSend(exchageEmail, "", email);
//	}
//}