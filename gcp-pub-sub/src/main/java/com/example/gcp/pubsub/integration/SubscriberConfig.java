package com.example.gcp.pubsub.integration;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Slf4j
@Configuration
class SubscriberConfig {

		private final String subscription;
		private final PubSubTemplate template;

		SubscriberConfig(
			@Value("${reservations.subscription:reservations-subscription}") String s,
			PubSubTemplate t) {
				this.subscription = s;
				this.template = t;
		}

		@Bean // <1>
		public PubSubInboundChannelAdapter messageChannelAdapter() {
				PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(
					template, this.subscription);
				adapter.setOutputChannel(this.incoming());
				adapter.setAckMode(AckMode.MANUAL);
				return adapter;
		}

		@Bean
		MessageChannel incoming() {
				return MessageChannels.publishSubscribe().get();
		}

		@Bean
		IntegrationFlow subscriberFlow() {
				return IntegrationFlows
					.from(this.incoming()) //<2>
					.handle(message -> { // <3>
							log.info("consumed new message: [" + message.getPayload() + "]");
							AckReplyConsumer consumer = message.getHeaders()
								.get(GcpPubSubHeaders.ACKNOWLEDGEMENT, AckReplyConsumer.class);
							consumer.ack();
					})
					.get();
		}
}
