package com.programmers.smrtstore.core.config;

import com.programmers.smrtstore.domain.auth.jwt.Jwt;
import com.programmers.smrtstore.domain.auth.jwt.JwtAuthenticationFilter;
import com.programmers.smrtstore.core.properties.JwtProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtProperties jwtProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .rememberMe(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .headers(AbstractHttpConfigurer::disable)
            .sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/auth/**").permitAll())
            .addFilterBefore(jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptionHandling -> {
                exceptionHandling.accessDeniedHandler(accessDeniedHandler());
                exceptionHandling.authenticationEntryPoint(authenticationEntryPoint());
            })
            .build();
    }

    @Bean
    public WebSecurityCustomizer ignoringWebSecurityCustomizer() {
        return web -> web.ignoring()
            .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**"))
            .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"))
            .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**"));
    }

    @Bean
    protected Jwt jwt() {
        return new Jwt(
            jwtProperties.getIssuer(),
            jwtProperties.getClientSecret(),
            jwtProperties.getAccessTokenExpiryHour(),
            jwtProperties.getRefreshTokenExpiryHour(),
            Date::new
        );
    }

    @Bean
    protected JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
            jwtProperties.getHeader(),
            jwt()
        );
    }

    @Bean
    protected AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Qualifier("handlerExceptionResolver")
            private HandlerExceptionResolver resolver;

            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                AccessDeniedException accessDeniedException) throws IOException, ServletException {
                resolver.resolveException(request, response, null, accessDeniedException);
            }
        };
    }

    @Bean
    protected AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Qualifier("handlerExceptionResolver")
            private HandlerExceptionResolver resolver;

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                AuthenticationException authException) throws IOException, ServletException {
                resolver.resolveException(request, response, null, authException);
            }
        };
    }
}