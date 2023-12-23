package com.programmers.smrtstore.core.config;

import com.programmers.smrtstore.core.properties.JwtProperties;
import com.programmers.smrtstore.domain.auth.jwt.Jwt;
import com.programmers.smrtstore.domain.auth.jwt.JwtAccessDeniedHandler;
import com.programmers.smrtstore.domain.auth.jwt.JwtAuthenticationEntryPoint;
import com.programmers.smrtstore.domain.auth.jwt.JwtAuthenticationFilter;
import com.programmers.smrtstore.domain.auth.jwt.JwtAuthenticationProvider;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtProperties jwtProperties;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ObjectPostProcessor<Object> objectPostProcessor;

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
                exceptionHandling.accessDeniedHandler(jwtAccessDeniedHandler);
                exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint);
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
    public AuthenticationManager authenticationManager(
        JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception {
        return new AuthenticationManagerBuilder(objectPostProcessor)
            .authenticationProvider(jwtAuthenticationProvider).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
