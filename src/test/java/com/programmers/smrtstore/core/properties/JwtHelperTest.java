package com.programmers.smrtstore.core.properties;

import static org.assertj.core.api.Assertions.assertThat;

import com.programmers.smrtstore.domain.auth.jwt.JwtHelper;
import com.programmers.smrtstore.domain.auth.jwt.JwtToken;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test JWT Token Create, Verify")
class JwtHelperTest {

    private static final Date DATE = new Date();
    private static final String ISSUER = "issuer";
    private static final String CLIENT_SECRET = "clientSecret";
    private static final int ACCESS_TOKEN_EXPIRY_HOUR = 1;
    private static final int REFRESH_TOKEN_EXPIRY_HOUR = 3;

    @DisplayName("Test Create Token")
    @Test
    void testCreateTokenSuccess() {
        // Arrange
        Long expectedUserId = 1L;
        String[] role = new String[]{"ROLE_USER"};
        JwtHelper jwtHelper = new JwtHelper(ISSUER, CLIENT_SECRET, ACCESS_TOKEN_EXPIRY_HOUR,
            REFRESH_TOKEN_EXPIRY_HOUR, () -> DATE);
        // Act
        JwtToken actualResult = jwtHelper.sign(expectedUserId, role);
        // Assert
        assertThat(actualResult.getUserId()).isEqualTo(expectedUserId);
        assertThat(actualResult.getRefreshTokenExpiryDate()).isEqualTo(
            new Date(DATE.getTime() + REFRESH_TOKEN_EXPIRY_HOUR * 3600000L));
    }

    @DisplayName("Test verify Token")
    @Test
    void testVerifyTokenSuccess(){
        // Arrange
        Long expectedUserId = 1L;
        String[] role = new String[]{"ROLE_USER"};
        JwtHelper jwtHelper = new JwtHelper(ISSUER, CLIENT_SECRET, ACCESS_TOKEN_EXPIRY_HOUR,
            REFRESH_TOKEN_EXPIRY_HOUR, () -> DATE);
        JwtToken jwtToken = jwtHelper.sign(expectedUserId, role);
        String accessToken = jwtToken.getAccessToken();
        // Act
        var actualResult = jwtHelper.verify(accessToken);
        // Assert
        assertThat(actualResult).containsKeys("userId", "roles", "exp", "iat");
        assertThat(actualResult.get("userId").asLong()).isEqualTo(expectedUserId);
        assertThat(actualResult.get("roles").asArray(String.class)).contains(role[0]);
    }

    // TODO: add Test for invalid Token type
    // TODO: add Test for expired Token
}