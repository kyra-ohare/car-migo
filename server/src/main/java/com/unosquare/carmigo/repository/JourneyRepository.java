package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Integer>
{
//    @Query(value = "")
    List<Journey> findJourneyByPassengerId(final int id);

    @Query(value = "SELECT j FROM Journey j INNER JOIN j.driver d WHERE d.id = ?1")
    List<Journey> findJourneyByDriverId(final int id);
}
