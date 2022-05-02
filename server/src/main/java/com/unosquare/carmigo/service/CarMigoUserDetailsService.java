package com.unosquare.carmigo.service;

import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CarMigoUserDetailsService implements UserDetailsService
{
    private final PlatformUserRepository platformUserRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException
    {
        final PlatformUser currentUser = platformUserRepository.findPlatformUserByEmail(email);
        return new User(currentUser.getEmail(), currentUser.getPassword(), new ArrayList<>());
    }
}
