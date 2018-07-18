package com.example.gcp.trace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class TraceServiceApplication {

		@GetMapping("/greeting/{id}")
		String greet(@PathVariable String id) {
				return "greetings, " + id + "!";
		}

		public static void main(String args[]) {
				SpringApplication.run(TraceServiceApplication.class, args);
		}
}