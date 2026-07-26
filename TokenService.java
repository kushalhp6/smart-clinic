package com.project.backend.services;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Generates a JWT token for the specified user email.
     *
     * @param email User's email address
     * @return Signed JWT token
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Returns the signing key used for JWT generation and validation.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Validates the supplied JWT token.
     *
     * @param token JWT token (with or without "Bearer " prefix)
     * @return true if valid; otherwise false
     */
    public boolean validateToken(String token) {

        if (token == null || token.isBlank()) {
            logger.warn("JWT validation failed: token is null or empty.");
            return false;
        }

        try {

            String jwt = token.startsWith("Bearer ")
                    ? token.substring(7)
                    : token;

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(jwt);

            logger.info("JWT token validated successfully.");
            return true;

        } catch (ExpiredJwtException e) {
            logger.error("JWT validation failed: token has expired.", e);

        } catch (UnsupportedJwtException e) {
            logger.error("JWT validation failed: unsupported token.", e);

        } catch (MalformedJwtException e) {
            logger.error("JWT validation failed: malformed token.", e);

        } catch (SecurityException e) {
            logger.error("JWT validation failed: invalid signature.", e);

        } catch (IllegalArgumentException e) {
            logger.error("JWT validation failed: empty claims.", e);

        } catch (JwtException e) {
            logger.error("JWT validation failed.", e);

        } catch (Exception e) {
            logger.error("Unexpected error while validating JWT.", e);
        }

        return false;
    }
}
