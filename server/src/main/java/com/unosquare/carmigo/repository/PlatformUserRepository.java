package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformUserRepository extends JpaRepository<PlatformUser, Integer> {

  PlatformUser findPlatformUserByEmail(final String email);
}
