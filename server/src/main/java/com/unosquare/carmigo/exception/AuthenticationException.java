package com.unosquare.carmigo.exception;

public class AuthenticationException extends RuntimeException
{
    private static final long serialVersionUID = -1547464948480870090L;

    public AuthenticationException(final String message)
    {
        super(message);
    }
}
