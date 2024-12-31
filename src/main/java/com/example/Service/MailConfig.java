package com.example.Service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.example.Entity.Email;

@Configuration
public class MailConfig {

	@Autowired
	private EmailService emailService;
	
	@Bean
	public JavaMailSender getJavaMailSender() {
		Email email = emailService.getEmail();
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(email.getSmtpServer());
		mailSender.setPort(email.getSmtpPort());
		mailSender.setUsername(email.getUsername());
		mailSender.setPassword(email.getPassword());
		
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
	}
}
