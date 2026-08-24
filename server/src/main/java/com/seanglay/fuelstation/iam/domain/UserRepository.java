package com.seanglay.fuelstation.iam.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

	Optional<User> findByUsername(String username);

	Optional<User> findById(UUID id);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	User save(User user);

}
