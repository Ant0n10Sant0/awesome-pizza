package com.awesome.pizza.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@SpringBootApplication
@EnableJdbcAuditing
public class AwesomePizzaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwesomePizzaApplication.class, args);
	} // main

} // AwesomePizzaApplication
