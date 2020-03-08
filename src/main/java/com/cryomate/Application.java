package com.cryomate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableEncryptableProperties
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
