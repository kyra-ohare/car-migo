package com.unosquare.carmigo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User
{
    private static final long serialVersionUID = 7027723211089349611L;

    private int id;

    private String userAccessStatus;

    public CustomUserDetails(final int id, final String username, final String password,
                             final String userAccessStatus, final Collection<? extends GrantedAuthority> authorities)
    {
        super(username, password, authorities);
        this.id = id;
        this.userAccessStatus = userAccessStatus;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUserAccessStatus()
    {
        return userAccessStatus;
    }

    public void setUserAccessStatus(String userAccessStatus)
    {
        this.userAccessStatus = userAccessStatus;
    }
}
