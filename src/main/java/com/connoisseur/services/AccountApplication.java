package com.connoisseur.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@SpringBootApplication
@EnableAutoConfiguration

@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
public class AccountApplication  {

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

}
