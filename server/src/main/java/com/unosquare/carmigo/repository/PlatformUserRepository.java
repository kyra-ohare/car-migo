package com.unosquare.carmigo.repository;

import com.unosquare.carmigo.entity.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformUserRepository extends JpaRepository<PlatformUser, Integer>
{
//    @Query("SELECT pu FROM PlatformUser pu WHERE pu.userAccessStatus = ?")
//    public PlatformUser findPlatformUserById(final int id);
}
