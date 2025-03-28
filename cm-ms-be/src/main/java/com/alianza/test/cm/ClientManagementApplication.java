package com.alianza.test.cm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class})
@CrossOrigin("*")
public class ClientManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientManagementApplication.class, args);
	}

}
