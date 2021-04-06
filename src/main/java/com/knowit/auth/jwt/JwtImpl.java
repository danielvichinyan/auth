package com.knowit.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.knowit.auth.domain.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtImpl {

    private final String secretJwt;

    private final int expirationTimeJwt;

    public JwtImpl(
            @Value("$auth.app.secretJwt") String secretJwt,
            @Value("$auth.app.expirationTimeJwt") int expirationTimeJwt
    ) {
        this.secretJwt = secretJwt;
        this.expirationTimeJwt = expirationTimeJwt;
    }

    public String generateJwtToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date((new Date()).getTime() + this.expirationTimeJwt))
                .withKeyId(user.getId())
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC512(this.secretJwt));
    }
}
