package org.jhely.money.base.service;

import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.jhely.money.base.domain.OtpToken;
import org.jhely.money.base.repository.OtpTokenRepository;

@Service
public class OtpService {
	private final OtpTokenRepository repo;
	private final MailService mailService;
	private final int ttlSeconds;
	private final int resendCooldownSeconds;
	private final Random random = new Random();

	public OtpService(OtpTokenRepository repo, MailService mailService,
			@Value("${app.security.otp.ttl-seconds:300}") int ttlSeconds,
			@Value("${app.security.otp.resend-cooldown-seconds:45}") int resendCooldownSeconds) {
		this.repo = repo;
		this.mailService = mailService;
		this.ttlSeconds = ttlSeconds;
		this.resendCooldownSeconds = resendCooldownSeconds;
	}
	
	@Scheduled(fixedDelay = 3600000) // hourly
	@Transactional
	public void purgeExpired() {
	  var now = Instant.now();
	  repo.findAll().stream()
	      .filter(t -> !t.isUsed() && t.getExpiresAt().isBefore(now))
	      .forEach(t -> { t.setUsed(true); repo.save(t); });
	}

	public void sendOtp(String email) {
// Enforce a minimal cooldown by checking last token timestamp
		var now = Instant.now();
		
		// Invalidate any prior active tokens for this email
		repo.markAllUsedForEmail(email.toLowerCase());
		
//		var existing = repo.findFirstByEmailAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(email, now);
//		if (existing.isPresent() && existing.get().getCreatedAt().isAfter(now.minusSeconds(resendCooldownSeconds))) {
//			return; // silently ignore within cooldown
//		}

		var code = generateCode();
		var token = new OtpToken();
		token.setEmail(email.toLowerCase());
		token.setCodeHash(hash(code));
		token.setExpiresAt(now.plusSeconds(ttlSeconds));
		repo.save(token);

		mailService.sendOtpEmail(email, code);
	}

	public boolean verifyOtp(String email, String code) {
		var now = Instant.now();
		var tokenOpt = repo.findFirstByEmailAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(email.toLowerCase(), now);
		if (tokenOpt.isEmpty())
			return false;
		var token = tokenOpt.get();
		boolean ok = token.getCodeHash().equals(hash(code)) && !token.isUsed() && token.getExpiresAt().isAfter(now);
		if (ok) {
			token.setUsed(true);
			repo.save(token);
// Invalidate others
			repo.markAllUsedForEmail(email.toLowerCase());
		}
		return ok;
	}

	private String generateCode() {
// 6-digit code
		int n = 100000 + random.nextInt(900000);
		return String.valueOf(n);
	}

	private String hash(String s) {
		try {
			var md = MessageDigest.getInstance("SHA-256");
			return HexFormat.of().formatHex(md.digest(s.getBytes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
