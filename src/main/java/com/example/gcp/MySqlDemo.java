package com.example.gcp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
	* @author <a href="mailto:josh@joshlong.com">Josh Long</a>
	*/
@Log4j2
@Component
public class MySqlDemo {

	private final JdbcTemplate jdbcTemplate;

	MySqlDemo(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void mysql() throws Exception {
		final List<Reservation> reservations = this.jdbcTemplate
			.query("select  * from reservations",
				(resultSet, i) -> new Reservation(resultSet.getLong("id"), resultSet.getString("name")));
		reservations.forEach(r -> log.info("MySQL reservation: " + r.toString()));
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Reservation {
		private Long id;
		private String reservationName;
	}
}
