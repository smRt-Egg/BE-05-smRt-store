package com.programmers.smrtstore.domain.user.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotEmpty
    private String principal;

    @NotEmpty
    private String credentials;
}
