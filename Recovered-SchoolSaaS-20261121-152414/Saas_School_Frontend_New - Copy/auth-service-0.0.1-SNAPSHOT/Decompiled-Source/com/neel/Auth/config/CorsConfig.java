/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Auth.config.CorsConfig
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.web.servlet.config.annotation.CorsRegistry
 *  org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */
package com.neel.Auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig
implements WebMvcConfigurer {
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(new String[]{"http://localhost:5174", "https://vaisacademy.com", "http://192.168.0.202:5174", "http://192.168.0.202:5173", "https://dev.webbixel.com", "http://192.168.0.185:3001", "https://collage.vaisacademy.com", "https://collage.vaisacademy.com/service4", "http://46.202.163.75:8092"}).allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"}).allowCredentials(true);
    }
}

