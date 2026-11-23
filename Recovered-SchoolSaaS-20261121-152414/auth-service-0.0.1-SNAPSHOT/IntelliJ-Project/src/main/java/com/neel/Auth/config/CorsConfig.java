/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Auth.config.CorsConfig
 *  jakarta.validation.constraints.NotNull
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.web.filter.CommonsRequestLoggingFilter
 *  org.springframework.web.servlet.config.annotation.CorsRegistry
 *  org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */
package com.neel.Auth.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig
implements WebMvcConfigurer {
    public void addCorsMappings(@NotNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://localhost:3001",
                        "http://127.0.0.1:3001",
                        "http://localhost:3000",
                        "http://127.0.0.1:3000",
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://localhost:5175",
                        "http://localhost:5176",
                        "http://192.168.*.*:*",
                        "https://*.vaisacademy.com",
                        "https://vaisacademy.com",
                        "https://*.webbixel.com",
                        "*"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(10000);
        loggingFilter.setIncludeHeaders(true);
        return loggingFilter;
    }
}

