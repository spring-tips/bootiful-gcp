package com.example.gcp.pubsub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.integration.annotation.MessagingGateway;

//@Slf4j
//@EnableConfigurationProperties(GcpApplication.GreetingsProperties.class)
//@IntegrationComponentScan
@SpringBootApplication
public class GcpApplication {


		/*
		@Bean
		ApplicationRunner run1(JdbcTemplate template) {

				RowMapper<Message> objectRowMapper = (rs, rowNum) -> new Message(
					rs.getLong("id"),
					rs.getString("name"),
					rs.getString("message"),
					rs.getString("image_uri"));

				return args ->
					template
						.query("select * from guestbook_message", objectRowMapper)
						.forEach(msg -> log.info("greetings: {}", msg.getMessage()));
		}
		@Bean
		ApplicationRunner run2(MessageRepository mr) {
				return args -> {
						mr.deleteAll();
						Stream.of("josh", "ray", "google.next")
							.map(msg -> new Message(UUID.randomUUID().toString(), msg, String.format("Hello, %s!", msg), "http://giphy.com/"))
							.forEach(mr::save);
				};
		}


		@Bean
		ApplicationRunner run3(MessageRepository mr, PubSubTemplate template, ObjectMapper om) {
				return args -> mr.findAll().forEach(msg -> {
						try {
								template.publish("messages", om.writeValueAsString(msg));
						}
						catch (JsonProcessingException e) {
								ReflectionUtils.rethrowRuntimeException(e);
						}
				});
		}

		@Bean
		ApplicationRunner run4(PubSubTemplate template) {
				return args -> template.subscribe("my-subscription", (pubsubMessage, ackReplyConsumer) -> {
						log.info("received greetings [" + new String(pubsubMessage.getData().toByteArray()) + "]");
						ackReplyConsumer.ack();
				});
		}


		@Bean
		ApplicationRunner run5(MessageRepository mr,
																									OutboundGateway gateway) {
				return ags -> mr.findAll().forEach(msg -> gateway.publishMessage(msg.getMessage()));
		}

		@Bean
		@ServiceActivator(inputChannel = "output")
		MessageHandler outputMessageHadler(PubSubTemplate pubsubTemplate) {
				return new PubSubMessageHandler(pubsubTemplate, "messages");
		}


		@RestController
		public static class GreetingsRestController {

				@GetMapping("/hi/{name}")
				String hi(@PathVariable String name) {
						return "hello " + name + "!";
				}
		}


		@Bean
		ApplicationRunner run6(MessageRepository messageRepository, RestTemplate rt) {
				return args ->
					messageRepository
						.findAll()
						.forEach(msg -> log.info("http response: " + rt.getForEntity(
							"http://localhost:8080/hi/{name}", String.class, msg.getName()).getBody()));
		}


		@Data
		@ConfigurationProperties(prefix = "")
		public static class GreetingsProperties {
				private String greeting;
		}

		@Bean
		ApplicationRunner run7(@Value("${greeting}") String message) {
				return arg -> log.info("greetings from Google Cloud Runtime Config: " + message);
		}

		@Bean
		ApplicationRunner run8(GreetingsProperties gp) {
				return args -> log.info("greetings from config props from Google Cloud Runtime Config: " + gp.getGreeting());
		}

		@Bean
		RestTemplate restTemplate() {
				return new RestTemplate();
		}
*/
		public static void main(String[] args) {
				SpringApplication.run(GcpApplication.class, args);
		}
}

@MessagingGateway(defaultRequestChannel = "output")
interface OutboundGateway {

		void publishMessage(String message);
}


@Data
@AllArgsConstructor
@NoArgsConstructor
//@Entity
@Table(name = "guestbook_message")
//@Table(name = "guestbook_message")
class Message {

		//		@Id
//		@GeneratedValue(strategy = GenerationType.IDENTITY)

		@Id
		@PrimaryKey
		private String id;

		private String name, message;

		@Column(name = "image_uri")
		private String imageUri;
}

@RepositoryRestResource
interface MessageRepository extends PagingAndSortingRepository<Message, String> {
}