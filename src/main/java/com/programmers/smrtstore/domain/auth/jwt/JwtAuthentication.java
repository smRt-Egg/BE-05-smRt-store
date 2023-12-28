package com.programmers.smrtstore.domain.auth.jwt;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtAuthentication {

    private Long userId;
    private String accessToken;
    private String refreshToken;
    private Date refreshTokenExpiryDate;
}
