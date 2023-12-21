package com.programmers.smrtstore.domain.auth.jwt;

import static io.micrometer.common.util.StringUtils.isNotEmpty;
import static java.util.Collections.emptyList;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String headerKey;
    private final Jwt jwt;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String token = Strings.isNotEmpty(request.getHeader(headerKey)) ? URLDecoder.decode(
            request.getHeader(headerKey), StandardCharsets.UTF_8) : null;

        if (token != null) {
            try {
                var claims = jwt.verify(token);

                String username = claims.get("username").asString();
                List<GrantedAuthority> authorities = getAuthorities(
                    claims.get("roles").asArray(String.class));

                if (isNotEmpty(username) && !authorities.isEmpty()) {
                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                        JwtAuthentication.builder()
                            .accessToken(token)
                            .username(username)
                            .build(), null, authorities
                    );
                    authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                log.warn("Jwt processing failed: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private List<GrantedAuthority> getAuthorities(String[] roles) {
        return roles == null || roles.length == 0 ? emptyList() : Arrays.stream(roles)
            .map(SimpleGrantedAuthority::new)
            .map(GrantedAuthority.class::cast)
            .toList();
    }
}
