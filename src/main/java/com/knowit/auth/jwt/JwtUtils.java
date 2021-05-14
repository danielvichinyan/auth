package com.knowit.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.knowit.auth.domain.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private final String jwtSecret;

    private final int jwtExpirationMs;

    public JwtUtils(
            @Value("${auth.app.jwtSecret}") String jwtSecret,
            @Value("${auth.app.jwtExpirationMs}") int jwtExpirationMs
    ) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateJwtToken(User user) {

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date((new Date()).getTime() + this.jwtExpirationMs))
                .withKeyId(user.getId())
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC512(this.jwtSecret));
    }
}