package com.programmers.smrtstore.core.properties;

import com.programmers.smrtstore.domain.user.application.UserService;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Jwt jwt;
    private final UserService userService;

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
            User user = userService.login(principal, credentials);
            List<GrantedAuthority> authorities = user.getAuthorities();
            JwtAuthentication token = getToken(user.getLoginId(), authorities);
            JwtAuthenticationToken authenticated =
                new JwtAuthenticationToken(token, null, authorities);
            authenticated.setDetails(user);
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
        return jwt.sign(username, roles);
    }
}
