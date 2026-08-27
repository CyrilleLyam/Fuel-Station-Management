package com.seanglay.fuelstation.iam.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.seanglay.fuelstation.iam.domain.User;
import com.seanglay.fuelstation.iam.domain.UserRepository;

@Repository
class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository jpaRepository;

	UserRepositoryImpl(UserJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public List<User> findAll() {
		return jpaRepository.findAll(Sort.by(Sort.Direction.ASC, "username"));
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return jpaRepository.findByUsername(username);
	}

	@Override
	public Optional<User> findById(UUID id) {
		return jpaRepository.findById(id);
	}

	@Override
	public boolean existsByUsername(String username) {
		return jpaRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return jpaRepository.existsByEmail(email);
	}

	@Override
	public User save(User user) {
		return jpaRepository.save(user);
	}

}
