package com.seanglay.fuelstation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.admin")
public record AdminBootstrapProperties(String username, String email, String password) {
}
