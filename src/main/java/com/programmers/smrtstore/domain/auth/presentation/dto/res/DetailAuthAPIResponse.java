package com.programmers.smrtstore.domain.auth.presentation.dto.res;

import com.programmers.smrtstore.domain.auth.application.dto.res.LoginResponse;
import com.programmers.smrtstore.domain.auth.jwt.JwtAuthentication;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailAuthAPIResponse {

    private String accessToken;
    private String refreshToken;
    private String refreshTokenExpiryDate;
    private String role;

    public static DetailAuthAPIResponse of(LoginResponse response,
        JwtAuthentication authentication) {
        return new DetailAuthAPIResponse(
            authentication.getAccessToken(),
            authentication.getRefreshToken(),
            authentication.getRefreshTokenExpiryDate().toString(),
            response.getRole().name()
        );
    }
}
