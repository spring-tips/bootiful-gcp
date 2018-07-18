package com.example.gcp.pubsub.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class GcpApplication {

		public static void main(String[] args) {
				SpringApplication.run(com.example.gcp.pubsub.template.GcpApplication.class, args);
		}
}

