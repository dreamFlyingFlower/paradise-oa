//package com.wy.receiver;
//
//import java.io.File;
//import java.io.IOException;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//import org.springframework.amqp.core.ExchangeTypes;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;
//
//import com.rabbitmq.client.Channel;
//import com.wy.model.old.Email;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author ParadiseWY
// * @description
// * @date 2020/3/18 10:50
// */
//@Configuration
//@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${config.rabbitmq.queue-email}"),
//		exchange = @Exchange(value = "${config.rabbitmq.exchange-email}", type = ExchangeTypes.FANOUT)))
//@Slf4j
//public class EmailReceiver {
//
//	@Value("${spring.mail.username}")
//	private String mailFrom;
//
//	@Autowired
//	private JavaMailSenderImpl mailSender;
//
//	@RabbitHandler
//	public void sendMail(Channel channel, Message msg, Email email) {
//		log.info(email.getFrom());
//		log.info(email.getTos());
//		log.info(email.getContent());
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setSubject("邮件主题");
//		message.setText("邮件内容");
//		message.setTo("mygodness30@sina.com");
//		message.setFrom(mailFrom);
//		mailSender.send(message);
//		try {
//			// Ack手动确认消息已经被消费,false表示只确认当前的消息,true表示队列中所有的消息都被确认
//			channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
//			/**
//			 * Ack返回false,第一个boolean表示是否确认当前消息,true表示该队列中所有消息,false表示当前消息
//			 * 第二个boolean表示丢弃消息或重新回到队列中,true表示重新回到队列,false表示丢弃
//			 */
//			// channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
//			// Ack拒绝消息
//			// channel.basicReject(msg.getMessageProperties().getDeliveryTag(), true);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//
//		// 复杂邮件可以使用MimeMessage等,查看SimpleUtils
//		// 邮件中带html
//		try {
//			MimeMessage htmlMessage = mailSender.createMimeMessage();
//			MimeMessageHelper mimeMessagehelper = new MimeMessageHelper(htmlMessage);
//			mimeMessagehelper.setTo(email.getTos());
//			mimeMessagehelper.setFrom(email.getFrom());
//			mimeMessagehelper.setSubject("Spring Boot Mail 邮件测试【HTML】");
//			// 启动html
//			mimeMessagehelper.setText(email.getContent(), true);
//			// 发送邮件
//			mailSender.send(htmlMessage);
//			System.out.print("邮件已发送");
//
//			// 邮件中带图片
//			MimeMessage mimeMessage = mailSender.createMimeMessage();
//			// multipart模式
//			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//			mimeMessageHelper.setTo(email.getTos());
//			mimeMessageHelper.setFrom(email.getFrom());
//			mimeMessageHelper.setSubject("Spring Boot Mail 邮件测试【图片】");
//			StringBuilder sb = new StringBuilder();
//			sb.append("spring 邮件测试hello!this is spring mail test。");
//			// cid为固定写法，imageId指定一个标识
//			sb.append("\"cid:imageId\"/>");
//			mimeMessageHelper.setText(sb.toString(), true);
//			// 设置imageId
//			FileSystemResource img = new FileSystemResource(new File("/Users/limbo/Desktop/1.jpg"));
//			mimeMessageHelper.addInline("imageId", img);
//			mailSender.send(mimeMessage);
//			System.out.println("邮件已发送");
//
//			// 邮件中带附件
//			MimeMessage attachMessage = mailSender.createMimeMessage();
//			MimeMessageHelper attachMessageHelper = new MimeMessageHelper(attachMessage, true, "utf-8");
//			attachMessageHelper.setTo(email.getTos());
//			attachMessageHelper.setFrom(email.getFrom());
//			attachMessageHelper.setSubject("Spring Boot Mail 邮件测试【附件】");
//			attachMessageHelper.setText(email.getContent(), true);
//			FileSystemResource file = new FileSystemResource(email.getFilePath());
//			attachMessageHelper.addAttachment(email.getFilename(), file);
//			mailSender.send(attachMessage);
//			System.out.println("邮件已发送");
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
//	}
//}