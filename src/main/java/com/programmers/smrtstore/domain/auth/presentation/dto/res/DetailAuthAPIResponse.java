package com.programmers.smrtstore.domain.auth.presentation.dto.res;

import com.programmers.smrtstore.domain.auth.application.dto.res.LoginResponse;
import com.programmers.smrtstore.domain.auth.jwt.JwtAuthentication;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailAuthAPIResponse {

    private String accessToken;
    private String refreshToken;
    private String username;
    private Role role;

    public static DetailAuthAPIResponse of(LoginResponse response, JwtAuthentication authentication) {
        return new DetailAuthAPIResponse(
            authentication.getAccessToken(),
            authentication.getRefreshToken(),
            response.getUsername(),
            response.getRole()
        );
    }
}
