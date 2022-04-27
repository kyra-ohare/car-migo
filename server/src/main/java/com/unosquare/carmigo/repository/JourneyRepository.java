package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Integer>
{
    List<Journey> findJourneysByDriverId(final int id);

    List<Journey> findJourneysByPassengersId(final int id);

    List<Journey> findJourneysByLocationFromIdAndLocationToIdAndDateTimeBetween(
            final int locationIdFrom, final int locationIdTo, final Instant dateTimeFrom, final Instant dateTimeTo);
}
