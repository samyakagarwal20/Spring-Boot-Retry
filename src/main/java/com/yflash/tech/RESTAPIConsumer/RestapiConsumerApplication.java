package com.yflash.tech.RESTAPIConsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class RestapiConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestapiConsumerApplication.class, args);
	}

}
