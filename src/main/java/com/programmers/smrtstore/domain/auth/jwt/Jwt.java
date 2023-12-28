package com.programmers.smrtstore.domain.auth.jwt;

import static com.auth0.jwt.JWT.create;
import static com.auth0.jwt.JWT.require;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.programmers.smrtstore.util.DateStrategy;
import java.util.Date;
import java.util.Map;


public class Jwt {

    private static final String USERNAME_STR = "username";
    private static final String ROLES_STR = "roles";
    private static final Long HOUR_MILLIS = 3600000L;

    private final String issuer;
    private final long accessTokenExpirySeconds;
    private final long refreshTokenExpirySeconds;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;
    private final DateStrategy dateStrategy;

    public Jwt(String issuer, String clientSecret, int accessTokenExpiryHour,
        int refreshTokenExpiryHour, DateStrategy dateStrategy) {
        this.issuer = issuer;
        this.accessTokenExpirySeconds = hourToMillis(accessTokenExpiryHour);
        this.refreshTokenExpirySeconds = hourToMillis(refreshTokenExpiryHour);
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = require(algorithm)
            .withIssuer(issuer)
            .build();
        this.dateStrategy = dateStrategy;
    }

    private static Long hourToMillis(int hour) {
        return hour * HOUR_MILLIS;
    }

    public Map<String, Claim> verify(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaims();
    }

    private static Date calculateExpirySeconds(Date nowDate, Long tokenExpirySeconds) {
        return new Date(nowDate.getTime() + tokenExpirySeconds);
    }

    public JwtAuthentication sign(String username, String[] roles) {
        Date nowDate = dateStrategy.create();
        String accessToken = create()
            .withIssuer(issuer)
            .withIssuedAt(nowDate)
            .withExpiresAt(calculateExpirySeconds(nowDate, accessTokenExpirySeconds))
            .withClaim(USERNAME_STR, username)
            .withArrayClaim(ROLES_STR, roles)
            .sign(algorithm);
        String refreshToken = create()
            .withIssuer(issuer)
            .withIssuedAt(nowDate)
            .withExpiresAt(calculateExpirySeconds(nowDate, refreshTokenExpirySeconds))
            .sign(algorithm);
        return JwtAuthentication.builder()
            .username(username)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .refreshTokenExpiryDate(calculateExpirySeconds(nowDate, refreshTokenExpirySeconds))
            .build();
    }

}
