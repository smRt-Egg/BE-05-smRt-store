package com.programmers.smrtstore.core.properties;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Jwt jwt;

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
        /*
         * TODO: userService 구현 필요 (feat. 찡비)
         *
        try {
            User user = userService.login(principal, credentials);
            List<GrantedAuthority> authorities = user.getGroup().getAuthorities();
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
        */
        return null;
    }

    private JwtAuthentication getToken(String username, List<GrantedAuthority> authorities) {
        String[] roles = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .toArray(String[]::new);
        return jwt.sign(Jwt.Claims.from(username, roles));
    }
}
