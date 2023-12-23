package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.user.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class DetailUserResponse {

    private String accessToken;
    private String refreshToken;
    private String username;
    private Role role;
}
