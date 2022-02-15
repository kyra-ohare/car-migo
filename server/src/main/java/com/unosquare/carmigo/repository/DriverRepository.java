package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer>
{
}
