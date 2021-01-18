//package com.wy.crl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.wy.model.vo.EmailVo;
//import com.wy.result.Result;
//import com.wy.service.EmailService;
//
//import io.swagger.annotations.Api;
//
///**
// * 邮件发送API
// * 
// * @author ParadiseWY
// * @date 2020年3月17日 下午4:36:26
// */
//@Api(tags = "邮件发送API")
//@RestController
//@RequestMapping("mail")
//public class EmailCrl {
//
//	@Autowired
//	private EmailService emailService;
//
//	@PostMapping("sendMail")
//	public Result<?> sendMail(@RequestBody EmailVo email) {
//		emailService.sendMail(email);
//		return Result.ok();
//	}
//}