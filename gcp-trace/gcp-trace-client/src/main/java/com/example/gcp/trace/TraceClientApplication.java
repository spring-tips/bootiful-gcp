package com.example.gcp.trace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.stream.IntStream;

@Slf4j
@SpringBootApplication
public class TraceClientApplication {

		@Component
		public static class Client {

				private final RestTemplate restTemplate;

				public Client(RestTemplate restTemplate) {
						this.restTemplate = restTemplate;
				}

				@EventListener(ApplicationReadyEvent.class)
				public void before() {
						IntStream
							.range(0, 100)
							.mapToObj(i ->
								restTemplate
									.getForEntity("http://localhost:8081/greeting/{id}", String.class, i)
									.getBody())
							.forEach(response -> log.info("result: " + response));
				}
		}

		@Bean
		RestTemplate restTemplate() {
				return new RestTemplate();
		}

		public static void main(String args[]) {
				SpringApplication.run(TraceClientApplication.class, args);
		}
}
