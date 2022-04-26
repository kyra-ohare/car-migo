package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Integer>, JpaSpecificationExecutor<Journey>
{
    List<Journey> findJourneysByDriverId(final int id);

    List<Journey> findJourneysByPassengersId(final int id);
}
