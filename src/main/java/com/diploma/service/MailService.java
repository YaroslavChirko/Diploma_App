package com.diploma.service;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailService {

	@Bean
	public JavaMailSenderImpl javaMailSender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		 sender.setHost("smtp.gmail.com");
		 sender.setPort(587);
		
		sender.setUsername("");
		sender.setPassword("");
		
		Properties props = sender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    //props.put("mail.debug", "true");
	    
	    return sender;
	}
	
	public static SimpleMailMessage composeMessage(String userEmail, String resetCode,String baseUrl) {
		SimpleMailMessage message = new SimpleMailMessage();
		String passwordResetUrl = baseUrl+"/user/"+resetCode+"/"+userEmail;
		message.setTo(userEmail);
		message.setSubject("Password Reset");
		message.setFrom("");
		message.setText("Password change was requested for \n User: "
				+userEmail+"\n on group creation app.\n"
				+ "if it was you follow: "+passwordResetUrl+" to change password,\n"		
				+ "Otherwise ignore this message.\n"
				+"This link will be valid for 2 hours.");
		return message;
	}
	
}
