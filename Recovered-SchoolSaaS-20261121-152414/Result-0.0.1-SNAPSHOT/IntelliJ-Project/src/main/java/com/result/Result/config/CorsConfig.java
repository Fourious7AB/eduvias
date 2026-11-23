package com.result.Result.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
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
                .allowCredentials(false)
                .maxAge(3600);
    }
}
