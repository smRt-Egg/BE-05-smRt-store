package com.programmers.smrtstore.domain.user.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {

    @NotEmpty
    private String principal;

    @NotEmpty
    private String credentials;
}
