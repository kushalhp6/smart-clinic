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

/**
 * Service responsible for generating and validating JSON Web Tokens (JWT).
 *
 * <p>This service provides authentication support for the Smart Clinic
 * application by generating signed JWT tokens for authenticated users
 * and validating tokens received with incoming requests.</p>
 *
 * <p>The generated tokens contain:</p>
 * <ul>
 *     <li>User email as the token subject</li>
 *     <li>Token creation timestamp</li>
 *     <li>Token expiration timestamp (24 hours)</li>
 * </ul>
 */
@Service
public class TokenService {

    /**
     * Logger used for recording token generation and validation events.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(TokenService.class);

    /**
     * Secret key loaded from application properties.
     * Used to sign and verify JWT tokens.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Generates a signed JWT token for the specified user.
     *
     * <p>The generated token contains the user's email,
     * issue timestamp, and expiration timestamp.</p>
     *
     * @param email authenticated user's email address
     * @return signed JWT token
     */
    public String generateToken(String email) {

        logger.info("Generating JWT token for user: {}", email);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Creates the secret signing key used for
     * JWT generation and validation.
     *
     * @return HMAC SHA signing key
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Validates a JWT token.
     *
     * <p>The supplied token may optionally include the
     * {@code Bearer } prefix. The method verifies the
     * token signature, expiration time, and integrity.</p>
     *
     * @param token JWT token received from the client
     * @return {@code true} if the token is valid;
     *         {@code false} otherwise
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
