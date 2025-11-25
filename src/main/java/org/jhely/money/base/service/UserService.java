package org.jhely.money.base.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import org.jhely.money.base.domain.UserAccount;
import org.jhely.money.base.repository.UserAccountRepository;

@Service
public class UserService {
	private final UserAccountRepository repo;

	public UserService(UserAccountRepository repo) {
		this.repo = repo;
	}

	public UserAccount upsertForEmail(String email, String displayName) {
		return repo.findByEmail(email.toLowerCase()).map(u -> {
			if (!u.getDisplayName().equals(displayName)) {
				u.setDisplayName(displayName);
			}
			return repo.save(u);
		}).orElseGet(() -> {
			var u = new UserAccount();
			u.setEmail(email.toLowerCase());
			u.setDisplayName(displayName);
			return repo.save(u);
		});
	}

	public Optional<UserAccount> findByEmail(String email) {
		return repo.findByEmail(email.toLowerCase());
	}
}