package org.jhely.money.base.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	private final JavaMailSender mailSender;

	@Value("${spring.mail.from:}")
	private String defaultFrom;

	public MailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendOtpEmail(String to, String code) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setTo(to);
			helper.setFrom(defaultFrom.isBlank() ? "no-reply@pro.jhely" : defaultFrom);
			helper.setSubject("Your login code");

			String html = """
					<div style='font-family: Arial, sans-serif'>
					<h2>Your one-time code</h2>
					<p>Use this code to sign in: <strong style='font-size:22px'>%s</strong></p>
					<p>This code expires in 5 minutes.</p>
					</div>
					""".formatted(code);
			helper.setText(html, true);

			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
// For dev environments without SMTP, just log
			System.out.println("[DEV] OTP for " + to + ": " + code);
		}
	}
}
