package com.wsd.diagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan({"com.wsd"})
public class DiagramApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiagramApplication.class, args);
	}

}

