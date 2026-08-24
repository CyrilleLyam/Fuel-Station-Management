package com.seanglay.fuelstation.config;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

@Configuration
public class CasbinConfig {

	@Bean
	Enforcer casbinEnforcer(DataSource dataSource) {
		Model model = new Model();
		model.loadModelFromText(readModelConf());

		try {
			JDBCAdapter adapter = new JDBCAdapter(dataSource, false, "casbin_rule", false);
			return new Enforcer(model, adapter);
		}
		catch (Exception ex) {
			throw new IllegalStateException("Failed to initialize Casbin JDBC adapter", ex);
		}
	}

	private String readModelConf() {
		try {
			return StreamUtils.copyToString(new ClassPathResource("casbin/model.conf").getInputStream(),
					StandardCharsets.UTF_8);
		}
		catch (IOException ex) {
			throw new UncheckedIOException("Failed to load Casbin model.conf", ex);
		}
	}

}
