package com.attendify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AttendifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendifyApplication.class, args);
	}

}
