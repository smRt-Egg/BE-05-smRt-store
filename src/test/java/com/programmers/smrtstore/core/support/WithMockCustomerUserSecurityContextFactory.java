package com.programmers.smrtstore.core.support;

import com.programmers.smrtstore.domain.auth.jwt.JwtAuthenticationContext;
import com.programmers.smrtstore.domain.auth.jwt.JwtHelper;
import com.programmers.smrtstore.domain.auth.jwt.JwtToken;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomerUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

    private final JwtHelper jwtHelper;

    public WithMockCustomerUserSecurityContextFactory() {
        this.jwtHelper = new JwtHelper("test",
            "test",
            1, 3,
            Date::new);
    }

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customUser.role()));
        JwtToken token = jwtHelper.sign(customUser.userId(),
            authorities.stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));
        var auth = new JwtAuthenticationContext(token, null, authorities);
        context.setAuthentication(auth);
        return context;
    }
}
