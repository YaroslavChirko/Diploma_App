package com.diploma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@ComponentScan({"com.diploma.dao","com.diploma.model","com.diploma.service"})
@Configuration
@EntityScan({"com.diploma.dao","com.diploma.model"})
@EnableJpaRepositories("com.diploma.dao")
@ComponentScan
@SpringBootApplication
public class DiplomaAppStandartStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiplomaAppStandartStorageApplication.class, args);
	}

}
