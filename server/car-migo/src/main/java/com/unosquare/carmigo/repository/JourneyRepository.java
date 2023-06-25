package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.Journey;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Queries database regarding the {@link Journey} entity.
 */
@Repository
public interface JourneyRepository extends JpaRepository<Journey, Integer> {

  List<Journey> findJourneysByDriverId(final int driverId);

  List<Journey> findJourneysByPassengersId(final int passengerId);

  List<Journey> findJourneysByLocationFromIdAndLocationToIdAndDateTimeBetween(
      final int locationIdFrom, final int locationIdTo, final Instant dateTimeFrom, final Instant dateTimeTo);
}
