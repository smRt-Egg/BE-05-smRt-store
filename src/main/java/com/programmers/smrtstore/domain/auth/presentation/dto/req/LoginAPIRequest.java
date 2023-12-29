package com.programmers.smrtstore.domain.auth.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LoginAPIRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
