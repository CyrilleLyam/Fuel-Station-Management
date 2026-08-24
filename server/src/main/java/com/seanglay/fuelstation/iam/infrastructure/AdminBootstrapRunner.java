package com.seanglay.fuelstation.iam.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.seanglay.fuelstation.config.AdminBootstrapProperties;
import com.seanglay.fuelstation.iam.application.CreateUserUseCase;
import com.seanglay.fuelstation.iam.domain.PolicyEnforcer;
import com.seanglay.fuelstation.iam.domain.UserRepository;

@Component
class AdminBootstrapRunner implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(AdminBootstrapRunner.class);

	private static final String ADMIN_ROLE = "ADMIN";

	private final UserRepository userRepository;

	private final CreateUserUseCase createUserUseCase;

	private final PolicyEnforcer policyEnforcer;

	private final AdminBootstrapProperties properties;

	AdminBootstrapRunner(UserRepository userRepository, CreateUserUseCase createUserUseCase,
			PolicyEnforcer policyEnforcer, AdminBootstrapProperties properties) {
		this.userRepository = userRepository;
		this.createUserUseCase = createUserUseCase;
		this.policyEnforcer = policyEnforcer;
		this.properties = properties;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (!userRepository.existsByUsername(properties.username())) {
			createUserUseCase.execute(properties.username(), properties.email(), properties.password());
			log.info("Seeded bootstrap admin user '{}'", properties.username());
		}

		if (!policyEnforcer.getRolesForUser(properties.username()).contains(ADMIN_ROLE)) {
			policyEnforcer.assignRoleToUser(properties.username(), ADMIN_ROLE);
		}

		policyEnforcer.grantPermissionToRole(ADMIN_ROLE, "iam", "admin");

		policyEnforcer.grantPermissionToRole(ADMIN_ROLE, "station", "create");
		policyEnforcer.grantPermissionToRole(ADMIN_ROLE, "station", "read");
		policyEnforcer.grantPermissionToRole(ADMIN_ROLE, "station", "update");
		policyEnforcer.grantPermissionToRole(ADMIN_ROLE, "station", "delete");
	}

}
