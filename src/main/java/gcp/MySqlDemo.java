package gcp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class MySqlDemo {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Reservation {
		private Long id;
		private String reservationName;
	}

	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<Reservation> reservationRowMapper =
		(resultSet, i) -> new Reservation(resultSet.getLong("id"), resultSet.getString("name"));

	public MySqlDemo(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void mysql() {
		log.info("starting the GCP-bound service.");
		final List<Reservation> reservations = this.jdbcTemplate
			.query("select * from reservations", this.reservationRowMapper);
		reservations.forEach(r -> log.info("reservation: " + r.toString()));
	}
}
