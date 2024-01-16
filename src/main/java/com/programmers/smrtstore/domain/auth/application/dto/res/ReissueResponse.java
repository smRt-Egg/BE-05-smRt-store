package com.programmers.smrtstore.domain.auth.application.dto.res;

import com.programmers.smrtstore.domain.auth.jwt.JwtToken;
import com.programmers.smrtstore.domain.user.domain.enums.Role;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReissueResponse {

    private String accessToken;
    private String refreshToken;
    private Date refreshTokenExpiryDate;
    private Role role;

    public static ReissueResponse of(JwtToken authentication, Role role) {
        return new ReissueResponse(
            authentication.getAccessToken(),
            authentication.getRefreshToken(),
            authentication.getRefreshTokenExpiryDate(),
            role
        );
    }
}
