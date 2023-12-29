package com.programmers.smrtstore.domain.auth.jwt;

import java.util.Collection;
import java.util.Objects;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class JwtAuthenticationContext extends AbstractAuthenticationToken {

    private final Object principal;
    private final String credentials;

    public JwtAuthenticationContext(Object principal, String credentials) {
        super(null);
        super.setAuthenticated(false);

        this.principal = principal;
        this.credentials = credentials;
    }

    public JwtAuthenticationContext(Object principal, String credentials,
        Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        JwtAuthenticationContext that = (JwtAuthenticationContext) o;
        return Objects.equals(principal, that.principal) && Objects.equals(
            credentials, that.credentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), principal, credentials);
    }
}
