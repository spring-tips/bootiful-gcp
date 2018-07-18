package com.example.gcp.pubsub.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Slf4j
@Configuration
class SubscriberConfig {

		private final PubSubTemplate template;
		private final String subscription;

		SubscriberConfig(PubSubTemplate template,
																			@Value("${reservations.subscription:reservations-subscription}") String s) {
				this.template = template;
				this.subscription = s;
		}

		@EventListener(ApplicationReadyEvent.class)
		public void start() {
				//<1>
				this.template.subscribe(this.subscription, (pubsubMessage, ackReplyConsumer) -> {
						log.info("consumed new message: [" + pubsubMessage.getData().toStringUtf8() + "]");
						ackReplyConsumer.ack();
				});
		}
}
