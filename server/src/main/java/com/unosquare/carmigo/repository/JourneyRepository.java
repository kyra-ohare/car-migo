package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Integer>
{
    @Query(value = "SELECT j FROM Journey j INNER JOIN j.driver d WHERE d.id = ?1")
    List<Journey> findJourneyByDriverId(final int id);

//  @Query(value = "SELECT pj.id, j.id, pj.passenger, "
//      + "j.createdDate, j.dateTime, j.locationIdFrom, j.locationIdTo, j.driver "
//      + "FROM Journey j "
//      + "INNER JOIN Journey j "
//      + "INNER JOIN pj.passenger p "
//      + "WHERE p.id = ?1")
//  List<Journey> findJourneyByPassengerId(final int id);
}

//  SELECT pj.id as passenger_journey_id, j.id as journey_id, pj.passenger_id, j.date_time,
//    j.location_id_from, j.location_id_to, j.driver_id
//    FROM journey j
//    INNER JOIN passenger_journey pj
//    ON pj.journey_id = j.id
//    WHERE pj.passenger_id = 1