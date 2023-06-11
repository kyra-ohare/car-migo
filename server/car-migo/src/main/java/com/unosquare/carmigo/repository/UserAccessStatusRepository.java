package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.UserAccessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Queries database regarding the {@link UserAccessStatus} entity.
 */
@Repository
public interface UserAccessStatusRepository extends JpaRepository<UserAccessStatus, Integer> {}
