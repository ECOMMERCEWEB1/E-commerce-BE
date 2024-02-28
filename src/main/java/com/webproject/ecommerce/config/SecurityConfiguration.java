package com.webproject.ecommerce.config;

import com.webproject.ecommerce.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/signup").permitAll()
                        .requestMatchers("/api/users").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                        .requestMatchers("/api/users/**").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.GET,"/api/products").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name(),Role.CLIENT.name())
                        .requestMatchers(HttpMethod.POST,"/api/products").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                        .requestMatchers("/api/products/**").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                        .anyRequest()
                        .authenticated()

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}