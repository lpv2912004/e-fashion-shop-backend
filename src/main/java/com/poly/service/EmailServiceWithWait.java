package com.poly.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.poly.model.Mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceWithWait {

	@Autowired
	private JavaMailSender mailSender;

	// Phần gửi mail set hàng chờ
	List<Mail> list = new ArrayList<Mail>();

	public void push(String from, String to, String subject, String content) {
		this.push(new Mail(from, to, subject, content));
	}

	public void push(Mail mail) {
		list.add(mail);
	}

	@Scheduled(fixedRate = 5000)
	public void run() throws MessagingException {
		while (!list.isEmpty()) {
			Mail mail = list.remove(0);
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

			helper.setTo(mail.getTo());
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getContent(), true);
			try {
				mailSender.send(message);
				System.out.println("Email sent successfully to: " + mail.getTo());
			} catch (Exception e) {
				System.err.println("Failed to send email to: " + mail.getTo());
				e.printStackTrace();
			}
		}
	}
}
