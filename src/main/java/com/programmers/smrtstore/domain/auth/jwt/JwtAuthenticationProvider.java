package com.programmers.smrtstore.domain.auth.jwt;

import com.programmers.smrtstore.domain.auth.application.AuthService;
import com.programmers.smrtstore.domain.auth.application.dto.res.LoginResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtHelper jwtHelper;
    private final AuthService authService;

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        return processUserAuthentication(
            String.valueOf(jwtAuthenticationToken.getPrincipal()),
            jwtAuthenticationToken.getCredentials()
        );
    }

    private Authentication processUserAuthentication(String principal, String credentials) {
        try {
            LoginResponse response = authService.login(principal, credentials);
            log.info("로그인 성공");
            List<GrantedAuthority> authorities = response.getAuthorities();
            JwtAuthentication token = getToken(response.getUsername(), authorities);
            JwtAuthenticationToken authenticated =
                new JwtAuthenticationToken(token, null, authorities);
            authenticated.setDetails(response);
            return authenticated;
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

    }

    private JwtAuthentication getToken(String username, List<GrantedAuthority> authorities) {
        String[] roles = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .toArray(String[]::new);
        return jwtHelper.sign(username, roles);
    }
}
