package gcp;

import org.springframework.data.repository.CrudRepository;

/**
	* @author <a href="mailto:josh@joshlong.com">Josh Long</a>
	*/
interface ReservationRepository extends CrudRepository<SpannerDemo.Reservation, String> {
}
