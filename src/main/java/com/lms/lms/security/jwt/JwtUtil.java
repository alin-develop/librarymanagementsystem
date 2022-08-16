package com.lms.lms.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String email, Collection<? extends GrantedAuthority> authorities) throws JWTCreationException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(60*60*24*5)))
                .withClaim("role", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuer("Library Management System")
                .sign(Algorithm.HMAC256(secret));
    }


    public String validateTokenAndRetrieveSubject(String token){
//        String encodedString = Base64.getEncoder().encodeToString(secret.getBytes());
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("Library Management System")
                .build();
        DecodedJWT jwt = verifier.verify(token);


        return jwt.getClaim("email").asString();
    }

}
