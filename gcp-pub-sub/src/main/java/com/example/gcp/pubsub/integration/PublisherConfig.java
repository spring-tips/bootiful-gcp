package com.example.gcp.pubsub.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Configuration
class PublisherConfig {

		private final String topic;
		private final PubSubTemplate template;

		public PublisherConfig(
			@Value("${reservations.topic:reservations}") String t,
			PubSubTemplate template) {
				this.topic = t;
				this.template = template;
		}

		@Bean
		IntegrationFlow publisherFlow() {
				return IntegrationFlows
					.from(this.outgoing()) // <1>
					.handle(this.pubSubMessageHandler()) // <2>
					.get();
		}

		@PostMapping("/publish/{name}")
		void publish(@PathVariable String name) {
				// <3>
				outgoing().send(MessageBuilder.withPayload(name).build());
		}

		@Bean
		SubscribableChannel outgoing() {
				return MessageChannels.direct().get();
		}

		@Bean
		PubSubMessageHandler pubSubMessageHandler() {
				return new PubSubMessageHandler(template, this.topic);
		}
}
