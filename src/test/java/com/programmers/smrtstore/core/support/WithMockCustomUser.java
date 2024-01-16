package com.programmers.smrtstore.core.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomerUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    long userId() default 1L;

    String role() default "ROLE_ADMIN";
}
