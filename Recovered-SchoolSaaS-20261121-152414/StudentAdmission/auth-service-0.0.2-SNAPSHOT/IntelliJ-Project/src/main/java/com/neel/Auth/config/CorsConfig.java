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
        registry.addMapping("/**").allowedOrigins(new String[]{"http://localhost:5174", "https://vaisacademy.com", "http://localhost:5173", "http://localhost:5175", "http://localhost:5176", "http://192.168.0.202:5174", "http://192.168.0.202:5173", "https://dev.webbixel.com", "http://192.168.0.185:3001", "https://collage.vaisacademy.com", "https://collage.vaisacademy.com/service4", "http://46.202.163.75:8092", "https://school.vaisacademy.com", "https://edu.vaisacademy.com", "https://edu.vaisacademy.com/service4", "https://school.vaisacademy.com/service4"}).allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"}).allowCredentials(true);
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

