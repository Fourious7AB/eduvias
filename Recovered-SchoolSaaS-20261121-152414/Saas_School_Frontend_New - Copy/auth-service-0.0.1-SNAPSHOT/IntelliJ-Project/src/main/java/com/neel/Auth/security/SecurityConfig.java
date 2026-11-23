/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Auth.request.JwtRequestFilter
 *  com.neel.Auth.security.SecurityConfig
 *  jakarta.servlet.Filter
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.security.config.annotation.web.builders.HttpSecurity
 *  org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
 *  org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer$AuthorizedUrl
 *  org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 *  org.springframework.security.web.SecurityFilterChain
 *  org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
 */
package com.neel.Auth.security;

import com.neel.Auth.request.JwtRequestFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.csrf(csrf -> csrf.disable()).authorizeRequests().requestMatchers(new String[]{"/api/auth/login", "/api/auth/logout", "/api/auth/register", "/api/auth/forgot-password", "/api/auth/reset-password", "/api/auth/delete/{username}"})).permitAll().anyRequest()).authenticated().and()).addFilterBefore((Filter)this.jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return (SecurityFilterChain)http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

