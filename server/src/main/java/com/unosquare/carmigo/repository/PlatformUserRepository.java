package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformUserRepository extends JpaRepository<PlatformUser, Integer>
{
    @Query("SELECT pu FROM PlatformUser pu JOIN FETCH pu.userAccessStatus WHERE pu.email = ?1")
    Optional<PlatformUser> findPlatformUserByEmail(final String email);
}
