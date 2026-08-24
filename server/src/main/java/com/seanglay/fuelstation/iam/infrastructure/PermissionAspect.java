package com.seanglay.fuelstation.iam.infrastructure;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.seanglay.fuelstation.iam.RequiresPermission;
import com.seanglay.fuelstation.iam.domain.PolicyEnforcer;

@Aspect
@Component
class PermissionAspect {

	private final PolicyEnforcer policyEnforcer;

	PermissionAspect(PolicyEnforcer policyEnforcer) {
		this.policyEnforcer = policyEnforcer;
	}

	/*
	 * Combining "@within(rp) || @annotation(rp)" into one advice leaves the bound
	 * parameter null under Spring's proxy-based AOP when only one side actually matches a
	 * given join point, so class-level and method-level annotations are handled by
	 * separate advice methods instead.
	 */
	@Before("@annotation(requiresPermission)")
	void checkMethodLevel(RequiresPermission requiresPermission) {
		checkPermission(requiresPermission);
	}

	@Before("@within(requiresPermission)")
	void checkClassLevel(RequiresPermission requiresPermission) {
		checkPermission(requiresPermission);
	}

	private void checkPermission(RequiresPermission requiresPermission) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new AccessDeniedException("Authentication required");
		}

		String username = authentication.getName();

		if (!policyEnforcer.hasPermission(username, requiresPermission.resource(), requiresPermission.action())) {
			throw new AccessDeniedException("User '%s' lacks permission %s:%s".formatted(username,
					requiresPermission.resource(), requiresPermission.action()));
		}
	}

}
