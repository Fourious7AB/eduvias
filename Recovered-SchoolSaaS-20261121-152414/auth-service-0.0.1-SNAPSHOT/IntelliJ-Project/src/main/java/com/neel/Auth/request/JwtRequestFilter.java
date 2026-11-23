/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Auth.request.JwtRequestFilter
 *  com.neel.Auth.service.JwtTokenService
 *  jakarta.servlet.FilterChain
 *  jakarta.servlet.ServletException
 *  jakarta.servlet.ServletRequest
 *  jakarta.servlet.ServletResponse
 *  jakarta.servlet.http.HttpServletRequest
 *  jakarta.servlet.http.HttpServletResponse
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 *  org.springframework.web.filter.OncePerRequestFilter
 */
package com.neel.Auth.request;

import com.neel.Auth.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter
extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtTokenService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            System.out.println("Token received: " + token);
        }
        filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
    }
}

