package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.PassengerJourney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerJourneyRepository extends JpaRepository<PassengerJourney, Integer>
{
    void deleteByJourneyIdAndPassengerId(final int journeyId, final int passengerId);
}
