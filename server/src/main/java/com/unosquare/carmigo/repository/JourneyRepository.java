package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Integer>
{
    List<Journey> findJourneysByDriverId(final int id);

    @Query("SELECT DISTINCT j FROM Journey j JOIN PassengerJourney pj ON j = pj.journey WHERE pj.passenger.id = ?1")
    List<Journey> findJourneysByPassengerId(final int id);
}
