package com.knowit.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.knowit.auth.domain.entities.Role;
import com.knowit.auth.domain.entities.User;
import com.knowit.auth.repositories.RoleRepository;
import com.knowit.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private final String jwtSecret;
    private final int jwtExpirationMs;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public JwtUtils(
            @Value("${auth.app.jwtSecret}") String jwtSecret,
            @Value("${auth.app.jwtExpirationMs}") int jwtExpirationMs,
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public String generateJwtToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date((new Date()).getTime() + this.jwtExpirationMs))
                .withKeyId(user.getId())
                .withIssuedAt(new Date())
                .withClaim("roles", user.getRoles().
                        stream().
                        map(Role::getName).
                        map(Object::toString).
                        collect(Collectors.joining(",")))
                .sign(Algorithm.HMAC512(this.jwtSecret));
    }
}
