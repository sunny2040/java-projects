package com.surge.vms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;




@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.surge.vms")
@EnableScheduling
public class VmsApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(VmsApplication.class, args);
	}
	
	
}
	