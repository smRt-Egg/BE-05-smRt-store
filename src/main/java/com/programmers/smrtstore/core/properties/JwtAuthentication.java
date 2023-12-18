package com.programmers.smrtstore.core.properties;

public class JwtAuthentication {

    private final String accessToken;
    private String refreshToken;
    private final String username;

    public JwtAuthentication(String accessToken, String username) {
        this.accessToken = accessToken;
        this.username = username;
    }

    JwtAuthentication(String accessToken, String refreshToken, String username) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
    }
}
