package gcp;

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
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SpannerDemo {

	private final ReservationRepository reservationRepository;

	public SpannerDemo(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Table(name = "reservations")
	public static class Reservation {

		@Id
		@PrimaryKey
		private String id;

		@Column(name = "name")
		private String reservationName;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void spanner(ApplicationReadyEvent e) {
		this.reservationRepository
			.findAll()
			.forEach(r -> log.info("reservation: " + r.toString()));
	}
}
