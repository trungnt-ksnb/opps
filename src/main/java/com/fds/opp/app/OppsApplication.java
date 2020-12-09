package com.fds.opp.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OppsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OppsApplication.class, args);
	}

}
