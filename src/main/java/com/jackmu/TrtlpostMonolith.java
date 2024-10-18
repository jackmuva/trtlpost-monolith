package com.jackmu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrtlpostMonolith {

	public static void main(String[] args) {
		SpringApplication.run(TrtlpostMonolith.class, args);
	}

}
