package com.seanglay.fuelstation.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(String signingPrivateKeyPath, String signingPublicKeyPath, String encryptionPrivateKeyPath,
		String encryptionPublicKeyPath, Duration accessTokenTtl, Duration refreshTokenTtl) {
}
