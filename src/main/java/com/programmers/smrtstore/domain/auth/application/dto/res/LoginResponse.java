package com.programmers.smrtstore.domain.auth.application.dto.res;

import com.programmers.smrtstore.domain.auth.domain.entity.Auth;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    private String username;
    private Role role;
    private List<GrantedAuthority> authorities;

    public static LoginResponse from(Auth auth) {
        return new LoginResponse(
            auth.getUsername(),
            auth.getUser().getRole(),
            auth.getUser().getAuthorities()
        );
    }
}
