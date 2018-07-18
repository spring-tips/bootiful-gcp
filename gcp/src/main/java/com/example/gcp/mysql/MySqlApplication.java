package com.example.gcp.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;

@Profile("mysql")
@SpringBootApplication
public class MySqlApplication {

		private final Log log = LogFactory.getLog(getClass());
		private final JdbcTemplate template;
		private final RowMapper<Reservation> rowMapper =
			(rs, rowNum) -> new Reservation(rs.getLong("id"), rs.getString("name"));

		MySqlApplication(JdbcTemplate template) {
				this.template = template;
		}

		@EventListener(ApplicationReadyEvent.class)
		public void ready() {
				Collection<Reservation> reservations = this.template.query("select * from reservations", this.rowMapper);
				reservations.forEach(reservation -> log.info("reservation: " + reservation.toString()));
		}

		public static void main(String args[]) {
				SpringApplication.run(MySqlApplication.class, args);
		}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Reservation {
		private Long id;
		private String reservationName;
}
