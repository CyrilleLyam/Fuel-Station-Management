package com.seanglay.fuelstation.iam.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

	List<User> findAll();

	Optional<User> findByUsername(String username);

	Optional<User> findById(UUID id);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	User save(User user);

}
