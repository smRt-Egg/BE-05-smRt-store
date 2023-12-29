package com.programmers.smrtstore.domain.auth.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ReissueAPIRequest {

    @NotEmpty
    private String username;
    @NotEmpty
    private String refreshToken;
}
