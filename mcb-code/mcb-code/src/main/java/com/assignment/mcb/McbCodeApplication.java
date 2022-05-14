package com.assignment.mcb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class McbCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(McbCodeApplication.class, args);
	}

}
