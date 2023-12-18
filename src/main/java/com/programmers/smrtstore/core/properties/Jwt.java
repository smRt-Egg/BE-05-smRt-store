package com.programmers.smrtstore.core.properties;

import static com.auth0.jwt.JWT.create;
import static com.auth0.jwt.JWT.require;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public final class Jwt {

    private static final String USERNAME = "username";
    private static final String ROLES = "roles";

    private final String issuer;

    private final String clientSecret;
    private final int accessTokenExpirySeconds;
    private final int refreshTokenExpirySeconds;

    private final Algorithm algorithm;

    private final JWTVerifier jwtVerifier;

    public Jwt(String issuer, String clientSecret, int accessTokenExpirySeconds, int refreshTokenExpirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = require(algorithm)
            .withIssuer(issuer)
            .build();
    }

    public JwtAuthentication sign(Claims claims) {
        Date now = new Date();

        String accessToken = create()
            .withIssuer(issuer)
            .withIssuedAt(now)
            .withExpiresAt(new Date(now.getTime() + accessTokenExpirySeconds * 1_000L))
            .withClaim(USERNAME, claims.username)
            .withArrayClaim(ROLES, claims.roles)
            .sign(algorithm);

        String refreshToken = create()
            .withIssuer(issuer)
            .withIssuedAt(now)
            .withExpiresAt(new Date(now.getTime() + refreshTokenExpirySeconds * 1_000L))
            .sign(algorithm);

        return new JwtAuthentication(accessToken, refreshToken, claims.username);
    }

    public Claims verify(String token) throws JWTVerificationException {
        return new Claims(jwtVerifier.verify(token));
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Claims {

        String username;
        String[] roles;
        Date iat;
        Date exp;

        Claims(DecodedJWT decodedJWT) {
            Claim usernameClaim = decodedJWT.getClaim(USERNAME);
            if (!usernameClaim.isNull()) {
                this.username = usernameClaim.asString();
            }
            Claim rolesClaim = decodedJWT.getClaim(ROLES);
            if (!rolesClaim.isNull()) {
                this.roles = rolesClaim.asArray(String.class);
            }
            this.iat = decodedJWT.getIssuedAt();
            this.exp = decodedJWT.getExpiresAt();
        }

        public static Claims from(String username, String[] roles) {
            Claims claims = new Claims();
            claims.username = username;
            claims.roles = roles;
            return claims;
        }

        public Map<String, Object> asMap() {
            Map<String, Object> map = new HashMap<>();
            map.put(USERNAME, username);
            map.put(ROLES, roles);
            map.put("iat", getIat());
            map.put("exp", getExp());
            return map;
        }

        long getIat() {
            return iat != null ? iat.getTime() : -1;
        }

        long getExp() {
            return exp != null ? exp.getTime() : -1;
        }

        void eraseIat() {
            iat = null;
        }

        void eraseExp() {
            exp = null;
        }

        @Override
        public String toString() {
            return "Claims{" +
                "username='" + username + '\'' +
                ", roles=" + Arrays.toString(roles) +
                ", iat=" + iat +
                ", exp=" + exp +
                '}';
        }
    }
}
