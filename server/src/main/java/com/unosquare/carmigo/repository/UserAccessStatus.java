package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccessStatus extends JpaRepository<UserStatus, Integer> {}
