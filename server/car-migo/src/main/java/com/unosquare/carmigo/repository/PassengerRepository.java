package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Queries database regarding the {@link Passenger} entity.
 */
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {}
