package com.drummond.IA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.drummond.IA")
public class IaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IaApplication.class, args);
	}

}
