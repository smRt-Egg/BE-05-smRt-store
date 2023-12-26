package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.auth.jwt.JwtAuthentication;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class DetailAuthResponse {

    private String accessToken;
    private String refreshToken;
    private String username;
    private Role role;

    public static DetailAuthResponse toDetailAuthResponse(JwtAuthentication authentication,
        User user) {
        return DetailAuthResponse.builder()
            .username(user.getAuth().getLoginId())
            .accessToken(authentication.getAccessToken())
            .refreshToken(authentication.getRefreshToken())
            .role(user.getRole())
            .build();
    }
}
