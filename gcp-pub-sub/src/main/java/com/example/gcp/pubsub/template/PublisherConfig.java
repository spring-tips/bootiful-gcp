package com.example.gcp.pubsub.template;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RestController
class PublisherConfig {

		private final PubSubTemplate template;
		private final String topic;

		PublisherConfig(PubSubTemplate template,
																		@Value("${reservations.topic:reservations}") String t) {
				this.template = template;
				this.topic = t;
		}

		// <1>
		@PostMapping("/publish/{name}")
		void publish(@PathVariable String name) {
				this.template.publish(this.topic, "Hello " + name + "!");
		}
}
