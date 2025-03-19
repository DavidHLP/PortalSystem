package com.david.hlp.SpringBootWork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootWorkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWorkApplication.class, args);
	}

}
