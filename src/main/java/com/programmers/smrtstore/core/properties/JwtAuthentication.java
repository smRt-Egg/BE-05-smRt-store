package com.programmers.smrtstore.core.properties;

import java.util.Date;
import lombok.Builder;

@Builder
public class JwtAuthentication {

    private String username;
    private String accessToken;
    private String refreshToken;
    private Date refreshTokenExpiryDate;
}
