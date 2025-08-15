package com.example.people.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret:}")
    private String secret;
    @Value("${jwt.expirationMillis:3600000}")
    private long expirationMillis;
    private SecretKey key;

    @PostConstruct
    public void init() {
        if (secret == null || secret.trim().isEmpty()) {
            key = Keys.hmacShaKeyFor("dev-secret-change-me-dev-secret-change-me-32b".getBytes());
        } else {
            key = Keys.hmacShaKeyFor(secret.getBytes());
        }
    }

    public String generateToken(UserDetails user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMillis);
        return Jwts.builder().setSubject(user.getUsername()).setIssuedAt(now).setExpiration(exp).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token) { return getAllClaims(token).getSubject(); }

    public boolean isTokenValid(String token, UserDetails user) { return extractUsername(token).equals(user.getUsername()) && !isTokenExpired(token); }

    private boolean isTokenExpired(String token) { return getAllClaims(token).getExpiration().before(new Date()); }

    private Claims getAllClaims(String token) { return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody(); }
}
