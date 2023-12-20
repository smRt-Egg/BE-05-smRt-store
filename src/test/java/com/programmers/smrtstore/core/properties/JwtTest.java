package com.programmers.smrtstore.core.properties;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test JWT Token Create, Verify")
class JwtTest {

    private static final Date DATE = new Date();
    private static final String ISSUER = "issuer";
    private static final String CLIENT_SECRET = "clientSecret";
    private static final int ACCESS_TOKEN_EXPIRY_HOUR = 1;
    private static final int REFRESH_TOKEN_EXPIRY_HOUR = 3;

    @DisplayName("Test Create Token")
    @Test
    void testCreateTokenSuccess() {
        // Arrange
        String expectedUsername = "test";
        String[] role = new String[]{"ROLE_USER"};
        Jwt jwt = new Jwt(ISSUER, CLIENT_SECRET, ACCESS_TOKEN_EXPIRY_HOUR,
            REFRESH_TOKEN_EXPIRY_HOUR, () -> DATE);
        // Act
        JwtAuthentication actualResult = jwt.sign(expectedUsername, role);
        // Assert
        assertThat(actualResult.getUsername()).isEqualTo(expectedUsername);
        assertThat(actualResult.getRefreshTokenExpiryDate()).isEqualTo(
            new Date(DATE.getTime() + REFRESH_TOKEN_EXPIRY_HOUR * 3600000L));
    }

    @DisplayName("Test verify Token")
    @Test
    void testVerifyTokenSuccess(){
        // Arrange
        String expectedUsername = "test";
        String[] role = new String[]{"ROLE_USER"};
        Jwt jwt = new Jwt(ISSUER, CLIENT_SECRET, ACCESS_TOKEN_EXPIRY_HOUR,
            REFRESH_TOKEN_EXPIRY_HOUR, () -> DATE);
        JwtAuthentication jwtAuthentication = jwt.sign(expectedUsername, role);
        String accessToken = jwtAuthentication.getAccessToken();
        // Act
        var actualResult = jwt.verify(accessToken);
        // Assert
        assertThat(actualResult).containsKeys("username", "roles", "exp", "iat");
        assertThat(actualResult.get("username").asString()).isEqualTo(expectedUsername);
        assertThat(actualResult.get("roles").asArray(String.class)).contains(role[0]);
    }

    // TODO: add Test for invalid Token type
    // TODO: add Test for expired Token
}