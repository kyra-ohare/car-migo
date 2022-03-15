package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.Journey;
import com.unosquare.carmigo.entity.PassengerJourney;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerJourneyRepository extends JpaRepository<PassengerJourney, Integer>
{
  // SELECT j FROM Journey j INNER JOIN j.driver d WHERE d.id = ?1
  @Query(value = "SELECT pj FROM PassengerJourney pj INNER JOIN pj.passenger p WHERE p.id = ?1")
  List<PassengerJourney> findJourneyByPassengerId(final int id);
}

//Result @Query(value = "SELECT pj FROM PassengerJourney pj INNER JOIN pj.passenger p WHERE p.id = ?1")
//PassengerJourney(
//    id=6,
//    passenger=Passenger(id=1,
//      platformUser=PlatformUser(id=1, createdDate=2021-12-24T00:00:00Z, firstName=John, lastName=Smith, dob=1970-02-23T00:00:00Z, email=john.smith@example.com, password=Pass1234!, phoneNumber=0287513626)),
//    journey=Journey(id=3, createdDate=2022-01-04T00:00:00Z, maxPassengers=4, dateTime=2022-01-04T15:30:00Z))
//
//PassengerJourney(
//    id=11,
//    passenger=Passenger(id=1,
//      platformUser=PlatformUser(id=1, createdDate=2021-12-24T00:00:00Z, firstName=John, lastName=Smith, dob=1970-02-23T00:00:00Z, email=john.smith@example.com, password=Pass1234!, phoneNumber=0287513626)),
//    journey=Journey(id=5, createdDate=2022-01-05T00:00:00Z, maxPassengers=3, dateTime=2022-12-02T08:15:00Z))



//  SELECT pj.id, j.id, pj.passenger, "
//    + "j.createdDate, j.dateTime, j.locationIdFrom, j.locationIdTo, j.driver "
//    + "FROM PassengerJourney pj "
//    + "INNER JOIN pj.journey j "
//    + "INNER JOIN pj.passenger p "
//    + "WHERE p.id = ?1

//  SELECT pj.id as passenger_journey_id, j.id as journey_id, pj.passenger_id, j.date_time,
//    j.location_id_from, j.location_id_to, j.driver_id
//    FROM journey j
//    INNER JOIN passenger_journey pj
//    ON pj.journey_id = j.id
//    WHERE pj.passenger_id = 1