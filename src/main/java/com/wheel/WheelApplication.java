package com.wheel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WheelApplication {

	public static void main(String[] args) {
		SpringApplication.run(WheelApplication.class, args);
	}

}
