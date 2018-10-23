package com.example.gcp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SpannerDemo {

	private final ReservationRepository reservationRepository;

	SpannerDemo(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void spanner() {
		this.reservationRepository
			.findAll()
			.forEach(r -> log.info("Spanner reservation: " + r.toString()));
	}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservations")
class Reservation {

	@Id
	@PrimaryKey
	private String id;

	@Column(name = "name")
	private String reservationName;
}

interface ReservationRepository extends CrudRepository<Reservation, String> {
}