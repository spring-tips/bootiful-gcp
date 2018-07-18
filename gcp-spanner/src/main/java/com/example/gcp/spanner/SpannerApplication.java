package com.example.gcp.spanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
public class SpannerApplication {

		private final ReservationRepository reservationRepository;

		SpannerApplication(ReservationRepository reservationRepository) {
				this.reservationRepository = reservationRepository;
		}

		@EventListener(ApplicationReadyEvent.class)
		public void setup() {

				// <1>
				this.reservationRepository.deleteAll();

				Stream
					.of("ray", "josh")
					.map(name -> new Reservation(UUID.randomUUID().toString(), name))
					.forEach(this.reservationRepository::save);
				this.reservationRepository.findAll().forEach(r -> log.info(r.toString()));
		}

		public static void main(String args[]) {
				SpringApplication.run(SpannerApplication.class, args);
		}
}

// <2>
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservations")
class Reservation {

		@Id
		@PrimaryKey
		private String id;
		private String name;
}

// <3>
@RepositoryRestResource
interface ReservationRepository extends PagingAndSortingRepository<Reservation, String> {
}