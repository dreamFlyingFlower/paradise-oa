//package com.wy.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.wy.base.AbstractService;
//import com.wy.model.old.Email;
//import com.wy.producer.EmailProducer;
//import com.wy.service.EmailService;
//
///**
// * @apiNote 邮件业务处理
// * @author ParadiseWY
// * @date 2020年3月19日 上午11:10:55
// */
//@Service
//public class EmailServiceImpl extends AbstractService<Email> implements EmailService{
//
//	@Autowired
//	private EmailProducer emailProducer;
//
//	public void sendMail(Email email) {
//		emailProducer.sendEmail(email);
//	}
//}