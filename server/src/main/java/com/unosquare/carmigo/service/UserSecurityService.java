package com.unosquare.carmigo.service;

import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.exception.AuthenticationException;
import com.unosquare.carmigo.exception.ResourceNotFoundException;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService
{
    private final PlatformUserRepository platformUserRepository;

    @Override
    public UserDetails loadUserByUsername(final String email)
    {
        final PlatformUser currentUser = platformUserRepository.findPlatformUserByEmail(email);
        if (currentUser != null) {
            final String accessStatus = currentUser.getUserAccessStatus().getStatus();
            if (StringUtils.isNotBlank(accessStatus)) {
                return getUserDetails(currentUser, accessStatus);
            }
        }
        throw new ResourceNotFoundException(String.format("Incorrect email (%s) and/or password", email));
    }

    /**
     * Returns the user along with their type of access which can be:<br>
     * * Active - user can use the application without restrictions.<br>
     * * Admin - user has admin privileges such as see other users' information.<br>
     * * Locked_out - user is locked out after 5 failed attempts.<br>
     * * Staged - account has been created but no email verification yet.<br>
     * * Suspended - user can see journeys and update profile. User cannot create/apply for journeys, accept/reject
     * passengers.
     *
     * @param currentUser  PlatformUser from the database
     * @param accessStatus the type of access this user has
     * @return the UserDetails
     */
    private UserDetails getUserDetails(final PlatformUser currentUser, final String accessStatus)
    {
        switch (accessStatus) {
            case "Active":
            case "Admin":
            case "Suspended":
                return new User(currentUser.getEmail(), currentUser.getPassword(), true, true,
                        true, true, getAuthorities(accessStatus));
            case "Locked_out":
                throw new AuthenticationException("User is locked out after 5 failed attempts.");
            case "Staged":
                throw new AuthenticationException("User needs to confirm the email.");
            default:
                throw new NoResultException("Unknown UserAccessStatus property.");
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final String accessStatus)
    {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(accessStatus));
        return authorities;
    }
}
