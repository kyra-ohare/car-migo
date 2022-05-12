package com.unosquare.carmigo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenUtils
{
    @Value("${application.secret.key}")
    private String key;

    public String generateToken(final UserDetails userDetails)
    {
        final Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public Boolean validateToken(final String token, final UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private String createToken(final Map<String, Object> claims, final String subject)
    {
        final Date now = Date.from(Instant.now());
        final Date in10Hours = Date.from(now.toInstant().plus(Duration.ofHours(10)));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(in10Hours)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String extractUsername(final String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(final String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    public <R> R extractClaim(final String token, final Function<Claims, R> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String token)
    {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(final String token)
    {
        return extractExpiration(token).before(Date.from(Instant.now()));
    }
}
