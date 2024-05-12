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
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults()).csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/login").permitAll()
                                .requestMatchers("/api/signup").permitAll()
                                .requestMatchers("/api/auth").permitAll()
                                .requestMatchers("/api/reset-password").permitAll()
                                .requestMatchers("/api/send-reset").permitAll()
                                .requestMatchers("/api/logout").authenticated()
                                .requestMatchers("/api/users/count").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                                .requestMatchers("/api/users").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                                .requestMatchers("/api/users/**").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                                .requestMatchers(HttpMethod.GET,"/api/products").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/products/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/products").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE,"/api/products/**").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                                .requestMatchers(HttpMethod.PUT,"/api/products/**").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                                .requestMatchers(HttpMethod.GET,"/api/product-categories").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/product-categories/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/product-categories").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                                .requestMatchers(HttpMethod.PUT,"/api/product-categories/**").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE,"/api/product-categories/**").hasAnyAuthority(Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                                .requestMatchers(HttpMethod.POST,"/api/product-order/**").hasAnyAuthority(Role.CLIENT.name())
                                .requestMatchers(HttpMethod.GET,"/api/product-order/**").hasAnyAuthority(Role.CLIENT.name(),Role.ADMIN.name(),Role.SUPER_ADMIN.name())
                                .requestMatchers(HttpMethod.PATCH,"/api/product-order/**").hasAnyAuthority(Role.CLIENT.name(),Role.ADMIN.name())
                                .requestMatchers("/api/invoices/**").hasAnyAuthority(Role.CLIENT.name(),Role.ADMIN.name())
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
