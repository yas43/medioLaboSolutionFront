package com.ykestdar.mediLaboSolutionFront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.web.client.*;

@SpringBootApplication
public class MediLaboSolutionFrontApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediLaboSolutionFrontApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
