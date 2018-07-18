package com.example.gcp.runtimeconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RuntimeConfigApplication {

		@RefreshScope // <1>
		@RestController
		public static class GreetingsRestController {

				private final String greetings;

				// <2>
				GreetingsRestController(@Value("${greeting}") String greetings) {
						this.greetings = greetings;
				}

				@GetMapping("/greeting")
				String greetings() {
						return this.greetings;
				}
		}

		public static void main(String[] args) {
				SpringApplication.run(RuntimeConfigApplication.class, args);
		}
}
