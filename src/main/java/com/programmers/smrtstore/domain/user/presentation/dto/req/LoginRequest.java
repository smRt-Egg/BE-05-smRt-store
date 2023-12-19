package com.programmers.smrtstore.domain.user.presentation.dto.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    private String principal;
    private String credentials;
}
