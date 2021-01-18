package com.wy.service;

import com.wy.model.vo.EmailVo;

/**
 * 邮件
 * 
 * @author ParadiseWY
 * @date 2020年3月19日 上午11:10:23
 */
public interface EmailService {

	/**
	 * 邮件发送
	 * 
	 * @param email 邮件实例
	 */
	void sendMail(EmailVo email);
}