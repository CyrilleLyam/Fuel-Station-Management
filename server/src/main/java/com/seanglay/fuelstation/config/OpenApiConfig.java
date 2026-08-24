package com.seanglay.fuelstation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI fuelStationOpenApi() {
		return new OpenAPI().info(new Info().title("Fuel Station Management API").version("v1"));
	}

}
