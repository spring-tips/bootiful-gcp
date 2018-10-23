package com.example.gcp;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
	* @author <a href="mailto:josh@joshlong.com">Josh Long</a>
	*/
@Log4j2
@Component
public class ConfigDemo {

	private final String greeting;

	ConfigDemo(@Value("${greeting}") String greeting) {
		this.greeting = greeting;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void config() {
		log.info("greeting: " + this.greeting);
	}
}
